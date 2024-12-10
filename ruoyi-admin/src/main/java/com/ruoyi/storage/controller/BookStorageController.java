package com.ruoyi.storage.controller;

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
import com.ruoyi.storage.domain.BookStorage;
import com.ruoyi.storage.service.IBookStorageService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 图书库存Controller
 * 
 * @author ruoyi
 * @date 2024-12-10
 */
@RestController
@RequestMapping("/storage/BookStorage")
public class BookStorageController extends BaseController
{
    @Autowired
    private IBookStorageService bookStorageService;

    /**
     * 查询图书库存列表
     */
    @PreAuthorize("@ss.hasPermi('storage:BookStorage:list')")
    @GetMapping("/list")
    public TableDataInfo list(BookStorage bookStorage)
    {
        startPage();
        List<BookStorage> list = bookStorageService.selectBookStorageList(bookStorage);
        return getDataTable(list);
    }

    /**
     * 导出图书库存列表
     */
    @PreAuthorize("@ss.hasPermi('storage:BookStorage:export')")
    @Log(title = "图书库存", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BookStorage bookStorage)
    {
        List<BookStorage> list = bookStorageService.selectBookStorageList(bookStorage);
        ExcelUtil<BookStorage> util = new ExcelUtil<BookStorage>(BookStorage.class);
        util.exportExcel(response, list, "图书库存数据");
    }

    /**
     * 获取图书库存详细信息
     */
    @PreAuthorize("@ss.hasPermi('storage:BookStorage:query')")
    @GetMapping(value = "/{storageId}")
    public AjaxResult getInfo(@PathVariable("storageId") Long storageId)
    {
        return success(bookStorageService.selectBookStorageByStorageId(storageId));
    }

    /**
     * 新增图书库存
     */
    @PreAuthorize("@ss.hasPermi('storage:BookStorage:add')")
    @Log(title = "图书库存", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BookStorage bookStorage)
    {
        return toAjax(bookStorageService.insertBookStorage(bookStorage));
    }

    /**
     * 修改图书库存
     */
    @PreAuthorize("@ss.hasPermi('storage:BookStorage:edit')")
    @Log(title = "图书库存", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BookStorage bookStorage)
    {
        return toAjax(bookStorageService.updateBookStorage(bookStorage));
    }

    /**
     * 删除图书库存
     */
    @PreAuthorize("@ss.hasPermi('storage:BookStorage:remove')")
    @Log(title = "图书库存", businessType = BusinessType.DELETE)
	@DeleteMapping("/{storageIds}")
    public AjaxResult remove(@PathVariable Long[] storageIds)
    {
        return toAjax(bookStorageService.deleteBookStorageByStorageIds(storageIds));
    }
}
