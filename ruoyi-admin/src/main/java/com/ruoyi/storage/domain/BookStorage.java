package com.ruoyi.storage.domain;

import java.time.LocalDate;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 图书库存对象 book_storage
 *
 * @author ruoyi
 * @date 2024-12-10
 */
public class BookStorage extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 库存ID */
    @Excel(name = "库存ID")
    private Long storageId;

    /** 藏书ID */
    @Excel(name = "藏书ID")
    private Long bookId;

    /** 图书馆ID */
    @Excel(name = "图书馆ID")
    private Long libraryId;

    /** 当前库存数量 */
    @Excel(name = "当前库存数量")
    private Long stock;

    /** 总藏书数量 */
    @Excel(name = "总藏书数量")
    private Long total;

    /** 藏书入库日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "藏书入库日期", width = 30, dateFormat = "yyyy-MM-dd")
    private LocalDate purchaseDate;

    public void setStorageId(Long storageId)
    {
        this.storageId = storageId;
    }

    public Long getStorageId()
    {
        return storageId;
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
    public void setStock(Long stock)
    {
        this.stock = stock;
    }

    public Long getStock()
    {
        return stock;
    }
    public void setTotal(Long total)
    {
        this.total = total;
    }

    public Long getTotal()
    {
        return total;
    }
    public void setPurchaseDate(LocalDate purchaseDate)
    {
        this.purchaseDate = purchaseDate;
    }

    public LocalDate getPurchaseDate()
    {
        return purchaseDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("storageId", getStorageId())
            .append("bookId", getBookId())
            .append("libraryId", getLibraryId())
            .append("stock", getStock())
            .append("total", getTotal())
            .append("purchaseDate", getPurchaseDate())
            .toString();
    }
}
