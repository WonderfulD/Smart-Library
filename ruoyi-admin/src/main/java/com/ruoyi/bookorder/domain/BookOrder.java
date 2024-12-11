package com.ruoyi.bookorder.domain;

import java.time.LocalDate;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 图书订购对象 book_order
 *
 * @author ruoyi
 * @date 2024-12-11
 */
public class BookOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 订购ID，主键 */
    @Excel(name = "订购ID，主键")
    private Long orderId;

    /** 图书ID */
    @Excel(name = "图书ID")
    private Long bookId;

    /** 图书馆ID */
    @Excel(name = "图书馆ID")
    private Long libraryId;

    /** 订购数量 */
    @Excel(name = "订购数量")
    private Long amount;

    /** 订购日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "订购日期", width = 30, dateFormat = "yyyy-MM-dd")
    private LocalDate orderDate;

    public void setOrderId(Long orderId)
    {
        this.orderId = orderId;
    }

    public Long getOrderId()
    {
        return orderId;
    }
    public void setBookId(Long bookId)
    {
        this.bookId = bookId;
    }

    public Long getBookId()
    {
        return bookId;
    }
    public void setLibraryId(Long libraryId)
    {
        this.libraryId = libraryId;
    }

    public Long getLibraryId()
    {
        return libraryId;
    }
    public void setAmount(Long amount)
    {
        this.amount = amount;
    }

    public Long getAmount()
    {
        return amount;
    }
    public void setOrderDate(LocalDate orderDate)
    {
        this.orderDate = orderDate;
    }

    public LocalDate getOrderDate()
    {
        return orderDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("orderId", getOrderId())
            .append("bookId", getBookId())
            .append("libraryId", getLibraryId())
            .append("amount", getAmount())
            .append("orderDate", getOrderDate())
            .toString();
    }
}
