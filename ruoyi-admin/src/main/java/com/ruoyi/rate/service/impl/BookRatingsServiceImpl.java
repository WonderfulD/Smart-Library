package com.ruoyi.rate.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.rate.mapper.BookRatingsMapper;
import com.ruoyi.rate.domain.BookRatings;
import com.ruoyi.rate.service.IBookRatingsService;

/**
 * 藏书评分Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-03-20
 */
@Service
public class BookRatingsServiceImpl implements IBookRatingsService 
{
    @Autowired
    private BookRatingsMapper bookRatingsMapper;

    /**
     * 查询藏书评分
     * 
     * @param ratingId 藏书评分主键
     * @return 藏书评分
     */
    @Override
    public BookRatings selectBookRatingsByRatingId(Long ratingId)
    {
        return bookRatingsMapper.selectBookRatingsByRatingId(ratingId);
    }

    /**
     * 查询藏书评分列表
     * 
     * @param bookRatings 藏书评分
     * @return 藏书评分
     */
    @Override
    public List<BookRatings> selectBookRatingsList(BookRatings bookRatings)
    {
        return bookRatingsMapper.selectBookRatingsList(bookRatings);
    }

    /**
     * 新增藏书评分
     * 
     * @param bookRatings 藏书评分
     * @return 结果
     */
    @Override
    public int insertBookRatings(BookRatings bookRatings)
    {
        return bookRatingsMapper.insertBookRatings(bookRatings);
    }

    /**
     * 修改藏书评分
     * 
     * @param bookRatings 藏书评分
     * @return 结果
     */
    @Override
    public int updateBookRatings(BookRatings bookRatings)
    {
        return bookRatingsMapper.updateBookRatings(bookRatings);
    }

    /**
     * 批量删除藏书评分
     * 
     * @param ratingIds 需要删除的藏书评分主键
     * @return 结果
     */
    @Override
    public int deleteBookRatingsByRatingIds(Long[] ratingIds)
    {
        return bookRatingsMapper.deleteBookRatingsByRatingIds(ratingIds);
    }

    /**
     * 删除藏书评分信息
     * 
     * @param ratingId 藏书评分主键
     * @return 结果
     */
    @Override
    public int deleteBookRatingsByRatingId(Long ratingId)
    {
        return bookRatingsMapper.deleteBookRatingsByRatingId(ratingId);
    }
}
