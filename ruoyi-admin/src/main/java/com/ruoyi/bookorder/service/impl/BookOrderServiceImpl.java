package com.ruoyi.bookorder.service.impl;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.bookorder.mapper.BookOrderMapper;
import com.ruoyi.bookorder.domain.BookOrder;
import com.ruoyi.bookorder.service.IBookOrderService;

/**
 * 图书订购Service业务层处理
 *
 * @author ruoyi
 * @date 2024-12-11
 */
@Service
public class BookOrderServiceImpl implements IBookOrderService
{
    @Autowired
    private BookOrderMapper bookOrderMapper;

    /**
     * 查询图书订购
     *
     * @param orderId 图书订购主键
     * @return 图书订购
     */
    @Override
    public BookOrder selectBookOrderByOrderId(Long orderId)
    {
        return bookOrderMapper.selectBookOrderByOrderId(orderId);
    }

    /**
     * 查询图书订购列表
     *
     * @param bookOrder 图书订购
     * @return 图书订购
     */
    @Override
    public List<BookOrder> selectBookOrderList(BookOrder bookOrder)
    {
        return bookOrderMapper.selectBookOrderList(bookOrder);
    }

    /**
     * 新增图书订购
     *
     * @param bookOrder 图书订购
     * @return 结果
     */
    @Override
    public int insertBookOrder(BookOrder bookOrder)
    {
        return bookOrderMapper.insertBookOrder(bookOrder);
    }

    /**
     * 修改图书订购
     *
     * @param bookOrder 图书订购
     * @return 结果
     */
    @Override
    public int updateBookOrder(BookOrder bookOrder)
    {
        return bookOrderMapper.updateBookOrder(bookOrder);
    }

    /**
     * 批量删除图书订购
     *
     * @param orderIds 需要删除的图书订购主键
     * @return 结果
     */
    @Override
    public int deleteBookOrderByOrderIds(Long[] orderIds)
    {
        return bookOrderMapper.deleteBookOrderByOrderIds(orderIds);
    }

    /**
     * 删除图书订购信息
     *
     * @param orderId 图书订购主键
     * @return 结果
     */
    @Override
    public int deleteBookOrderByOrderId(Long orderId)
    {
        return bookOrderMapper.deleteBookOrderByOrderId(orderId);
    }

    /**
     * 计算指定图书馆在截止日期前的订购数量总和
     *
     * @param libraryId   图书馆ID
     * @param specificDay 截止日期
     * @return 订购数量总和
     */
    @Override
    public Integer selectTotalAmountByLibraryIdAndDate(Long libraryId, LocalDate specificDay) {
        return bookOrderMapper.selectTotalAmountByLibraryIdAndDate(libraryId, specificDay);
    }
}
