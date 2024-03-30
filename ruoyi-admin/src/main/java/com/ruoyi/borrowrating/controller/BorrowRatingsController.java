package com.ruoyi.borrowrating.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.borrowrating.domain.BorrowRatings;
import com.ruoyi.borrowrating.service.IBorrowRatingsService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 借阅评分Controller
 * 
 * @author ruoyi
 * @date 2024-03-30
 */
@RestController
@RequestMapping("/borrowrating/BorrowRating")
public class BorrowRatingsController extends BaseController
{
    @Autowired
    private IBorrowRatingsService borrowRatingsService;

    /**
     * 查询借阅评分列表
     */
//    @PreAuthorize("@ss.hasPermi('borrowrating:BorrowRating:list')")
    @GetMapping("/list")
    public TableDataInfo list(BorrowRatings borrowRatings)
    {
        startPage();
        List<BorrowRatings> list = borrowRatingsService.selectBorrowRatingsList(borrowRatings);
        return getDataTable(list);
    }

    /**
     * 导出借阅评分列表
     */
//    @PreAuthorize("@ss.hasPermi('borrowrating:BorrowRating:export')")
    @Log(title = "借阅评分", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BorrowRatings borrowRatings)
    {
        List<BorrowRatings> list = borrowRatingsService.selectBorrowRatingsList(borrowRatings);
        ExcelUtil<BorrowRatings> util = new ExcelUtil<BorrowRatings>(BorrowRatings.class);
        util.exportExcel(response, list, "借阅评分数据");
    }

    /**
     * 获取借阅评分详细信息
     */
//    @PreAuthorize("@ss.hasPermi('borrowrating:BorrowRating:query')")
    @GetMapping(value = "/{borrowId}")
    public AjaxResult getInfo(@PathVariable("borrowId") Long borrowId)
    {
        return success(borrowRatingsService.selectBorrowRatingsByBorrowId(borrowId));
    }

    /**
     * 新增借阅评分
     */
//    @PreAuthorize("@ss.hasPermi('borrowrating:BorrowRating:add')")
    @Log(title = "借阅评分", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BorrowRatings borrowRatings)
    {
        return toAjax(borrowRatingsService.insertBorrowRatings(borrowRatings));
    }

    /**
     * 修改借阅评分
     */
//    @PreAuthorize("@ss.hasPermi('borrowrating:BorrowRating:edit')")
    @Log(title = "借阅评分", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BorrowRatings borrowRatings)
    {
        return toAjax(borrowRatingsService.updateBorrowRatings(borrowRatings));
    }

    /**
     * 删除借阅评分
     */
//    @PreAuthorize("@ss.hasPermi('borrowrating:BorrowRating:remove')")
    @Log(title = "借阅评分", businessType = BusinessType.DELETE)
	@DeleteMapping("/{borrowIds}")
    public AjaxResult remove(@PathVariable Long[] borrowIds)
    {
        return toAjax(borrowRatingsService.deleteBorrowRatingsByBorrowIds(borrowIds));
    }
}
