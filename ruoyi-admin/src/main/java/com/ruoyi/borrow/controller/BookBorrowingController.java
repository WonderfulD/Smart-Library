package com.ruoyi.borrow.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.utils.SecurityUtils;
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
import com.ruoyi.borrow.domain.BookBorrowing;
import com.ruoyi.borrow.service.IBookBorrowingService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 图书借阅信息Controller
 * 
 * @author ruoyi
 * @date 2024-03-12
 */
@RestController
@RequestMapping("/borrow/BookBorrowing")
public class BookBorrowingController extends BaseController
{
    @Autowired
    private IBookBorrowingService bookBorrowingService;

    /**
     * 查询图书借阅信息列表
     */
    @PreAuthorize("@ss.hasPermi('borrow:BookBorrowing:list')")
    @GetMapping("/list")
    public TableDataInfo list(BookBorrowing bookBorrowing)
    {
        startPage();
        List<BookBorrowing> list = bookBorrowingService.selectBookBorrowingList(bookBorrowing);
        return getDataTable(list);
    }

    /**
     * 根据当前登录管理员所在图书馆ID，查询图书借阅信息列表
     */
    @PreAuthorize("@ss.hasPermi('borrow:BookBorrowing:list')")
    @GetMapping("/listByDept")
    public TableDataInfo listByDept(BookBorrowing bookBorrowing) {
        startPage();
        bookBorrowing.setLibraryId(SecurityUtils.getDeptId()); // 设置当前用户所在部门ID
        List<BookBorrowing> list = bookBorrowingService.selectBookBorrowingListByDept(bookBorrowing);
        return getDataTable(list);
    }

    /**
     * 导出图书借阅信息列表
     */
    @PreAuthorize("@ss.hasPermi('borrow:BookBorrowing:export')")
    @Log(title = "图书借阅信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BookBorrowing bookBorrowing)
    {
        List<BookBorrowing> list = bookBorrowingService.selectBookBorrowingList(bookBorrowing);
        ExcelUtil<BookBorrowing> util = new ExcelUtil<BookBorrowing>(BookBorrowing.class);
        util.exportExcel(response, list, "图书借阅信息数据");
    }

    /**
     * 获取图书借阅信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('borrow:BookBorrowing:query')")
    @GetMapping(value = "/{borrowId}")
    public AjaxResult getInfo(@PathVariable("borrowId") Long borrowId)
    {
        return success(bookBorrowingService.selectBookBorrowingByBorrowId(borrowId));
    }

    /**
     * 新增图书借阅信息
     */
    @PreAuthorize("@ss.hasPermi('borrow:BookBorrowing:add')")
    @Log(title = "图书借阅信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BookBorrowing bookBorrowing)
    {
        return toAjax(bookBorrowingService.insertBookBorrowing(bookBorrowing));
    }

    /**
     * 修改图书借阅信息
     */
    @PreAuthorize("@ss.hasPermi('borrow:BookBorrowing:edit')")
    @Log(title = "图书借阅信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BookBorrowing bookBorrowing)
    {
        return toAjax(bookBorrowingService.updateBookBorrowing(bookBorrowing));
    }

    /**
     * 删除图书借阅信息
     */
    @PreAuthorize("@ss.hasPermi('borrow:BookBorrowing:remove')")
    @Log(title = "图书借阅信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{borrowIds}")
    public AjaxResult remove(@PathVariable Long[] borrowIds)
    {
        return toAjax(bookBorrowingService.deleteBookBorrowingByBorrowIds(borrowIds));
    }
}
