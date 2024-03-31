package com.ruoyi.rate.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
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
