package com.ruoyi.rate.service.impl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ruoyi.RedisObject;
import com.ruoyi.book.domain.Books;
import com.ruoyi.book.service.IBooksService;
import com.ruoyi.rate.domain.BookRatings;
import com.ruoyi.rate.mapper.BookRatingsMapper;
import com.ruoyi.rate.service.IBookRatingsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.ruoyi.RedisConstants.*;

/**
 * 藏书评分Service业务层处理
 *
 * @author ruoyi
 * @date 2024-03-20
 */
@Slf4j
@Service
public class BookRatingsServiceImpl implements IBookRatingsService {
    //逻辑过期缓存重建线程池
    public static final ExecutorService CACHE_THREAD_POOL = Executors.newFixedThreadPool(10);

    @Autowired
    private BookRatingsMapper bookRatingsMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private IBookRatingsService bookRatingsService;

    @Autowired
    private IBooksService booksService;

    /**
     * 查询藏书评分
     *
     * @param ratingId 藏书评分主键
     * @return 藏书评分
     */
    @Override
    public BookRatings selectBookRatingsByRatingId(Long ratingId) {
        return bookRatingsMapper.selectBookRatingsByRatingId(ratingId);
    }


    public String getRating(Long bookId) {
        BookRatings queryBookRatings = new BookRatings();
        queryBookRatings.setBookId(bookId);
        List<BookRatings> list = bookRatingsService.selectBookRatingsList(queryBookRatings);
        if (list.isEmpty()) return null;
        double averageRating = list.stream().mapToDouble(BookRatings::getRating).average().orElse(0.0);
        return String.valueOf((double) Math.round(averageRating * 10) / 10);
    }

    /**
     * 查询藏书总体评分
     * 缓存空值解决缓存穿透
     *
     * @param bookId 藏书ID
     * @return 藏书总体评分
     */
    @Override
    public String getAverageRating(Long bookId) {
        String key = CACHE_BOOKRATING_KEY + bookId;
        String ratingStr = stringRedisTemplate.opsForValue().get(key);
        if (StrUtil.isNotEmpty(ratingStr)) {
            if (CACHE_NULL_PLACEHOLDER.equals(ratingStr)) {
                return null;
            } else return ratingStr;
        } else { //Redis未命中
            ratingStr = getRating(bookId);
            //存Redis
            if (ratingStr != null) { //查得到
                stringRedisTemplate.opsForValue().set(key, ratingStr, CACHE_BOOKRATING_EX, TimeUnit.MINUTES);
                return ratingStr;
            } else { //查不到
                stringRedisTemplate.opsForValue().set(key, CACHE_NULL_PLACEHOLDER, CACHE_NULL_EX, TimeUnit.MINUTES);
                return null;
            }
        }
    }

    /**
     * 查询藏书总体评分
     * 逻辑过期解决缓存击穿
     *
     * @param bookId 藏书ID
     * @return 藏书总体评分
     */
    @Override
    public String getAverageRatingWithLogicalExpiration(Long bookId) {
        String key = CACHE_BOOKRATING_KEY + bookId;
        String ratingJson = stringRedisTemplate.opsForValue().get(key);
        if (StrUtil.isEmpty(ratingJson)) {
            return null;
        }
        //检查过期时间
        //获取评分
        RedisObject redisObject = JSONUtil.toBean(ratingJson, RedisObject.class);
        String ratingStr = (String) redisObject.getData();
        //获取过期时间
        LocalDateTime exTime = redisObject.getLocalDateTime();
        if (LocalDateTime.now().isBefore(exTime)) {
            //没过期
            return ratingStr;
        }
        //过期了，缓存重建
        String lockKey = LOCK_BOOKRATING_KEY + bookId;
        boolean tryLock = tryLock(lockKey, LOCK_BOOKRATING_EX);
        if (tryLock) {
            String ratingJson2 = stringRedisTemplate.opsForValue().get(key);
            RedisObject redisObject2 = JSONUtil.toBean(ratingJson2, RedisObject.class);
            LocalDateTime exTime2 = redisObject2.getLocalDateTime();
            if (LocalDateTime.now().isBefore(exTime2)) {
                //二次检查时间
                unlock(lockKey);
                return (String) redisObject2.getData();
            }
            //调用新线程执行缓存重建
            CACHE_THREAD_POOL.submit(() -> {
                //重建缓存
                try {
                    RedisObject newredisObject = new RedisObject();
                    newredisObject.setData(getRating(bookId));
                    newredisObject.setLocalDateTime(LocalDateTime.now().plusMinutes(30));
                    stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(newredisObject));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    unlock(lockKey);
                }
            });
        }
        return ratingStr;
    }


    public boolean tryLock(String key, Long time) {
        Boolean result = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", time, TimeUnit.SECONDS);
        return BooleanUtil.isTrue(result);
    }

    public void unlock(String key) {
        stringRedisTemplate.delete(key);
    }


    /**
     * 查询藏书评分列表
     *
     * @param bookRatings 藏书评分
     * @return 藏书评分
     */
    @Override
    public List<BookRatings> selectBookRatingsList(BookRatings bookRatings) {
        return bookRatingsMapper.selectBookRatingsList(bookRatings);
    }

    /**
     * 新增藏书评分
     *
     * @param bookRatings 藏书评分
     * @return 结果
     */
    @Override
    public int insertBookRatings(BookRatings bookRatings) {
        int response = bookRatingsMapper.insertBookRatings(bookRatings);
        Long bookId = bookRatings.getBookId();
        String key = CACHE_BOOKRATING_KEY + bookId;
        String ratingStr = getRating(bookId);
        //存Redis
        stringRedisTemplate.opsForValue().set(key, ratingStr, CACHE_BOOKRATING_EX, TimeUnit.MINUTES);
        return response;
    }

    /**
     * 修改藏书评分
     *
     * @param bookRatings 藏书评分
     * @return 结果
     */
    @Override
    public int updateBookRatings(BookRatings bookRatings) {
        int response = bookRatingsMapper.updateBookRatings(bookRatings);
        Long bookId = bookRatings.getBookId();
        String key = CACHE_BOOKRATING_KEY + bookId;
        String ratingStr = getRating(bookId);
        //存Redis
        stringRedisTemplate.opsForValue().set(key, ratingStr, CACHE_BOOKRATING_EX, TimeUnit.MINUTES);
        return response;
    }

    /**
     * 批量删除藏书评分
     *
     * @param ratingIds 需要删除的藏书评分主键
     * @return 结果
     */
    @Override
    public int deleteBookRatingsByRatingIds(Long[] ratingIds) {
        return bookRatingsMapper.deleteBookRatingsByRatingIds(ratingIds);
    }

    /**
     * 将json对象列表转化为 List<Map<String, Object>>
     *
     * @param jsonStr
     * @return
     */
    public List<Map<String, Object>> parseToListOfMaps(String jsonStr) {
        List<Map<String, Object>> list = new ArrayList<>();
        JSONArray jsonArray = JSONUtil.parseArray(jsonStr);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Map<String, Object> map = JSONUtil.toBean(jsonObject, Map.class);
            list.add(map);
        }
        return list;
    }


    /**
     * 根据图书馆Id查询藏书总体评分列表
     *
     * @param libraryId 图书馆ID
     * @return
     */
    @Override
    public List<Map<String, Object>> getAverageRatingListForBooks(Long libraryId) {
        String key = CACHE_LIBRARY_BOOKRATING_KEY + libraryId;
        String ratingStr = stringRedisTemplate.opsForValue().get(key);
        if (!StrUtil.isNotEmpty(ratingStr)) {
            List<BookRatings> allRatings = selectBookRatingsList(new BookRatings());

            Set<Long> bookIds = allRatings.stream().map(BookRatings::getBookId).collect(Collectors.toSet());

            List<Map<String, Object>> responseList = new ArrayList<>();

            for (Long bookId : bookIds) {
                BookRatings queryBookRatings = new BookRatings();
                queryBookRatings.setBookId(bookId);
                List<BookRatings> list = selectBookRatingsList(queryBookRatings);
                double averageRating = list.stream().mapToDouble(BookRatings::getRating).average().orElse(0.0);

                // 获取图书详细信息
                Books bookInfo = booksService.selectBooksByBookId(bookId);

                // 创建一个包含图书详细信息和平均评分的Map
                if (libraryId != null && !Objects.equals(bookInfo.getLibraryId(), libraryId)) {
                    continue;
                }

                // 创建一个包含图书详细信息和平均评分的Map
                Map<String, Object> bookDetails = new HashMap<>();
                bookDetails.put("bookId", bookId);
                bookDetails.put("title", bookInfo.getTitle());
                bookDetails.put("author", bookInfo.getAuthor());
                bookDetails.put("isbn", bookInfo.getIsbn());
                bookDetails.put("publisher", bookInfo.getPublisher());
                bookDetails.put("publishDate", bookInfo.getPublishDate());
                bookDetails.put("category", bookInfo.getCategory());
                bookDetails.put("description", bookInfo.getDescription());
                bookDetails.put("language", bookInfo.getLanguage());
                bookDetails.put("pages", bookInfo.getPages());
                bookDetails.put("coverUrl", bookInfo.getCoverUrl());
                bookDetails.put("edition", bookInfo.getEdition());
                bookDetails.put("status", bookInfo.getStatus());
                bookDetails.put("averageRating", averageRating);

                responseList.add(bookDetails);
            }

            // 在返回之前对responseList按照averageRating从大到小进行排序
            responseList.sort((map1, map2) -> Double.compare((double) map2.get("averageRating"), (double) map1.get("averageRating")));

            ratingStr = JSONUtil.toJsonStr(responseList);

            //存Redis
            stringRedisTemplate.opsForValue().set(key, ratingStr, CACHE_BOOKRATING_EX, TimeUnit.MINUTES);
        }
        return parseToListOfMaps(ratingStr);
    }

    /**
     * 删除藏书评分信息
     *
     * @param ratingId 藏书评分主键
     * @return 结果
     */
    @Override
    public int deleteBookRatingsByRatingId(Long ratingId) {
        return bookRatingsMapper.deleteBookRatingsByRatingId(ratingId);
    }
}
