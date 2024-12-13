package com.ruoyi.rate.controller;

import com.ruoyi.book.service.IBooksService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.rate.domain.BookRatings;
import com.ruoyi.rate.service.IBookRatingsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 藏书评分Controller
 *
 * @author ruoyi
 * @date 2024-03-20
 */
@Slf4j
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
        String averageRating = bookRatingsService.getAverageRating(bookId);
        // 创建一个Map来存放需要返回的数据
        Map<String, Object> data = new HashMap<>();
        if (averageRating == null) {
            data.put("averageRating", 0);
        }else data.put("averageRating", Double.valueOf(averageRating));
        return success(data);
    }

    /**
     * 根据图书馆Id查询藏书总体评分列表
     *
     */
    @GetMapping("/averageRatingListByLibraryId")
    public AjaxResult getAverageRatingListByLibraryId() {
        Long libraryId = SecurityUtils.getDeptId();
        List<Map<String, Object>> responseList = bookRatingsService.getAverageRatingListForBooks(libraryId);
        return success(responseList);
    }

    /**
     * 查询所有图书馆藏书总体评分列表
     *
     */
    @GetMapping("/averageRatingList")
    public AjaxResult getAverageRatingList() {
        List<Map<String, Object>> responseList = bookRatingsService.getAverageRatingListForBooks(null);
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
