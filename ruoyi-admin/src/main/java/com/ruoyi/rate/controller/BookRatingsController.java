package com.ruoyi.rate.controller;

import java.util.*;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.book.domain.Books;
import com.ruoyi.book.service.IBooksService;
import com.ruoyi.borrow.domain.BookBorrowing;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.rate.domain.BookRatings;
import com.ruoyi.rate.service.IBookRatingsService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 藏书评分Controller
 *
 * @author ruoyi
 * @date 2024-03-20
 */
@RestController
@RequestMapping("/rate/BookRatings")
public class BookRatingsController extends BaseController
{
    @Autowired
    private IBookRatingsService bookRatingsService;

    @Autowired
    private IBooksService booksService;

    /**
     * 查询藏书评分列表
     */
//    @PreAuthorize("@ss.hasPermi('rate:BookRatings:list')")
    @GetMapping("/list")
    public TableDataInfo list(BookRatings bookRatings)
    {
        startPage();
        List<BookRatings> list = bookRatingsService.selectBookRatingsList(bookRatings);
        return getDataTable(list);
    }

    /**
     * 查询藏书总体评分
     *
     */
    @GetMapping("/averageRating")
    public AjaxResult getAverageRating(@RequestParam Long bookId) {
        BookRatings queryBookRatings = new BookRatings();
        queryBookRatings.setBookId(bookId);
        List<BookRatings> list = bookRatingsService.selectBookRatingsList(queryBookRatings);
        double averageRating = list.stream()
                .mapToDouble(BookRatings::getRating)
                .average()
                .orElse(0.0);

        // 创建一个Map来存放需要返回的数据
        Map<String, Object> data = new HashMap<>();
        data.put("averageRating", averageRating);

        // 使用一个参数的success方法
        return success(data);
    }

    /**
     * 根据图书馆Id查询藏书总体评分列表
     *
     */
    @GetMapping("/averageRatingList")
    public AjaxResult getAverageRatingList() {
        List<BookRatings> allRatings = bookRatingsService.selectBookRatingsList(new BookRatings());

        Set<Long> bookIds = allRatings.stream()
                .map(BookRatings::getBookId)
                .collect(Collectors.toSet());

        List<Map<String, Object>> responseList = new ArrayList<>();

        for (Long bookId : bookIds) {
            BookRatings queryBookRatings = new BookRatings();
            queryBookRatings.setBookId(bookId);
            List<BookRatings> list = bookRatingsService.selectBookRatingsList(queryBookRatings);
            double averageRating = list.stream()
                    .mapToDouble(BookRatings::getRating)
                    .average()
                    .orElse(0.0);

            // 获取图书详细信息
            Books bookInfo = booksService.selectBooksByBookId(bookId);

            if (!Objects.equals(bookInfo.getLibraryId(), SecurityUtils.getDeptId())) {
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

        return success(responseList);
    }


    /**
     * 导出藏书评分列表
     */
//    @PreAuthorize("@ss.hasPermi('rate:BookRatings:export')")
    @Log(title = "藏书评分", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BookRatings bookRatings)
    {
        List<BookRatings> list = bookRatingsService.selectBookRatingsList(bookRatings);
        ExcelUtil<BookRatings> util = new ExcelUtil<BookRatings>(BookRatings.class);
        util.exportExcel(response, list, "藏书评分数据");
    }

    /**
     * 获取藏书评分详细信息
     */
//    @PreAuthorize("@ss.hasPermi('rate:BookRatings:query')")
    @GetMapping(value = "/{ratingId}")
    public AjaxResult getInfo(@PathVariable("ratingId") Long ratingId)
    {
        return success(bookRatingsService.selectBookRatingsByRatingId(ratingId));
    }



    /**
     * 新增藏书评分
     */
//    @PreAuthorize("@ss.hasPermi('rate:BookRatings:add')")
    @Log(title = "藏书评分", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BookRatings bookRatings)
    {
        return toAjax(bookRatingsService.insertBookRatings(bookRatings));
    }

    /**
     * 修改藏书评分
     */
//    @PreAuthorize("@ss.hasPermi('rate:BookRatings:edit')")
    @Log(title = "藏书评分", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BookRatings bookRatings)
    {
        return toAjax(bookRatingsService.updateBookRatings(bookRatings));
    }

    /**
     * 删除藏书评分
     */
//    @PreAuthorize("@ss.hasPermi('rate:BookRatings:remove')")
    @Log(title = "藏书评分", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ratingIds}")
    public AjaxResult remove(@PathVariable Long[] ratingIds)
    {
        return toAjax(bookRatingsService.deleteBookRatingsByRatingIds(ratingIds));
    }
}
