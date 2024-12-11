package com.ruoyi.bookorder.controller;

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
import com.ruoyi.bookorder.domain.BookOrder;
import com.ruoyi.bookorder.service.IBookOrderService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 图书订购Controller
 * 
 * @author ruoyi
 * @date 2024-12-11
 */
@RestController
@RequestMapping("/bookorder/BookOrder")
public class BookOrderController extends BaseController
{
    @Autowired
    private IBookOrderService bookOrderService;

    /**
     * 查询图书订购列表
     */
    @PreAuthorize("@ss.hasPermi('bookorder:BookOrder:list')")
    @GetMapping("/list")
    public TableDataInfo list(BookOrder bookOrder)
    {
        startPage();
        List<BookOrder> list = bookOrderService.selectBookOrderList(bookOrder);
        return getDataTable(list);
    }

    /**
     * 导出图书订购列表
     */
    @PreAuthorize("@ss.hasPermi('bookorder:BookOrder:export')")
    @Log(title = "图书订购", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BookOrder bookOrder)
    {
        List<BookOrder> list = bookOrderService.selectBookOrderList(bookOrder);
        ExcelUtil<BookOrder> util = new ExcelUtil<BookOrder>(BookOrder.class);
        util.exportExcel(response, list, "图书订购数据");
    }

    /**
     * 获取图书订购详细信息
     */
    @PreAuthorize("@ss.hasPermi('bookorder:BookOrder:query')")
    @GetMapping(value = "/{orderId}")
    public AjaxResult getInfo(@PathVariable("orderId") Long orderId)
    {
        return success(bookOrderService.selectBookOrderByOrderId(orderId));
    }

    /**
     * 新增图书订购
     */
    @PreAuthorize("@ss.hasPermi('bookorder:BookOrder:add')")
    @Log(title = "图书订购", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BookOrder bookOrder)
    {
        return toAjax(bookOrderService.insertBookOrder(bookOrder));
    }

    /**
     * 修改图书订购
     */
    @PreAuthorize("@ss.hasPermi('bookorder:BookOrder:edit')")
    @Log(title = "图书订购", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BookOrder bookOrder)
    {
        return toAjax(bookOrderService.updateBookOrder(bookOrder));
    }

    /**
     * 删除图书订购
     */
    @PreAuthorize("@ss.hasPermi('bookorder:BookOrder:remove')")
    @Log(title = "图书订购", businessType = BusinessType.DELETE)
	@DeleteMapping("/{orderIds}")
    public AjaxResult remove(@PathVariable Long[] orderIds)
    {
        return toAjax(bookOrderService.deleteBookOrderByOrderIds(orderIds));
    }
}
