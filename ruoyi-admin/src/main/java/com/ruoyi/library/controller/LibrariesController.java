package com.ruoyi.library.controller;

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
import com.ruoyi.library.domain.Libraries;
import com.ruoyi.library.service.ILibrariesService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 图书馆信息Controller
 * 
 * @author ruoyi
 * @date 2024-03-12
 */
@RestController
@RequestMapping("/library/LibraryInfo")
public class LibrariesController extends BaseController
{
    @Autowired
    private ILibrariesService librariesService;

    /**
     * 查询图书馆信息列表
     */
    @PreAuthorize("@ss.hasPermi('library:LibraryInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(Libraries libraries)
    {
        startPage();
        List<Libraries> list = librariesService.selectLibrariesList(libraries);
        return getDataTable(list);
    }

    /**
     * 导出图书馆信息列表
     */
    @PreAuthorize("@ss.hasPermi('library:LibraryInfo:export')")
    @Log(title = "图书馆信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Libraries libraries)
    {
        List<Libraries> list = librariesService.selectLibrariesList(libraries);
        ExcelUtil<Libraries> util = new ExcelUtil<Libraries>(Libraries.class);
        util.exportExcel(response, list, "图书馆信息数据");
    }

    /**
     * 获取图书馆信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('library:LibraryInfo:query')")
    @GetMapping(value = "/{libraryId}")
    public AjaxResult getInfo(@PathVariable("libraryId") Long libraryId)
    {
        return success(librariesService.selectLibrariesByLibraryId(libraryId));
    }

    /**
     * 新增图书馆信息
     */
    @PreAuthorize("@ss.hasPermi('library:LibraryInfo:add')")
    @Log(title = "图书馆信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Libraries libraries)
    {
        return toAjax(librariesService.insertLibraries(libraries));
    }

    /**
     * 修改图书馆信息
     */
    @PreAuthorize("@ss.hasPermi('library:LibraryInfo:edit')")
    @Log(title = "图书馆信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Libraries libraries)
    {
        return toAjax(librariesService.updateLibraries(libraries));
    }

    /**
     * 删除图书馆信息
     */
    @PreAuthorize("@ss.hasPermi('library:LibraryInfo:remove')")
    @Log(title = "图书馆信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{libraryIds}")
    public AjaxResult remove(@PathVariable Long[] libraryIds)
    {
        return toAjax(librariesService.deleteLibrariesByLibraryIds(libraryIds));
    }
}
