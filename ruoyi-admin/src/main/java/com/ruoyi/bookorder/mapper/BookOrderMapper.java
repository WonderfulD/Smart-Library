package com.ruoyi.bookorder.mapper;

import java.time.LocalDate;
import java.util.List;
import com.ruoyi.bookorder.domain.BookOrder;
import org.apache.ibatis.annotations.Param;

/**
 * 图书订购Mapper接口
 *
 * @author ruoyi
 * @date 2024-12-11
 */
public interface BookOrderMapper
{
    /**
     * 查询图书订购
     *
     * @param orderId 图书订购主键
     * @return 图书订购
     */
    public BookOrder selectBookOrderByOrderId(Long orderId);

    /**
     * 查询图书订购列表
     *
     * @param bookOrder 图书订购
     * @return 图书订购集合
     */
    public List<BookOrder> selectBookOrderList(BookOrder bookOrder);

    /**
     * 新增图书订购
     *
     * @param bookOrder 图书订购
     * @return 结果
     */
    public int insertBookOrder(BookOrder bookOrder);

    /**
     * 修改图书订购
     *
     * @param bookOrder 图书订购
     * @return 结果
     */
    public int updateBookOrder(BookOrder bookOrder);

    /**
     * 删除图书订购
     *
     * @param orderId 图书订购主键
     * @return 结果
     */
    public int deleteBookOrderByOrderId(Long orderId);

    /**
     * 批量删除图书订购
     *
     * @param orderIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBookOrderByOrderIds(Long[] orderIds);


    /**
     * 计算指定图书馆在截止日期前的订购数量总和
     *
     * @param libraryId   图书馆ID
     * @param specificDay 截止日期
     * @return 订购数量总和
     */
    public Integer selectTotalAmountByLibraryIdAndDate(@Param("libraryId") Long libraryId,
                                                @Param("specificDay") LocalDate specificDay);

    /**
     * 删除指定图书馆和图书ID列表的订购信息
     *
     * @param libraryId 图书馆ID
     * @param bookIds 图书ID列表
     */
    public void deleteBookOrdersByLibraryIdAndBookIds(@Param("libraryId") Long libraryId, @Param("bookIds") Long[] bookIds);
}
