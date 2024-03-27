package com.ruoyi.borrow.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 图书借阅信息对象 BookBorrowing
 * 
 * @author ruoyi
 * @date 2024-03-12
 */
public class BookBorrowing extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 借阅号 */
    @Excel(name = "借阅号")
    private Long borrowId;

    /** 书籍ID */
    @Excel(name = "书籍ID")
    private Long bookId;

    /** 读者ID */
    @Excel(name = "读者ID")
    private Long readerId;

    /** 图书馆ID */
    private Long libraryId;

    /** 借出日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "借出日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date borrowDate;

    /** 应还日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "应还日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date dueDate;

    /** 实际还书日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "实际还书日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date returnDate;

    /** 逾期罚款 */
    @Excel(name = "逾期罚款")
    private BigDecimal fine;

    /** 借阅备注 */
    @Excel(name = "借阅备注")
    private String comments;


    /** 借阅状态 */
    @Excel(name = "借阅状态")
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setBorrowId(Long borrowId)
    {
        this.borrowId = borrowId;
    }

    public Long getBorrowId() 
    {
        return borrowId;
    }
    public void setBookId(Long bookId) 
    {
        this.bookId = bookId;
    }

    public Long getBookId() 
    {
        return bookId;
    }
    public void setReaderId(Long readerId) 
    {
        this.readerId = readerId;
    }

    public Long getReaderId() 
    {
        return readerId;
    }
    public void setLibraryId(Long libraryId) 
    {
        this.libraryId = libraryId;
    }

    public Long getLibraryId() 
    {
        return libraryId;
    }
    public void setBorrowDate(Date borrowDate) 
    {
        this.borrowDate = borrowDate;
    }

    public Date getBorrowDate() 
    {
        return borrowDate;
    }
    public void setDueDate(Date dueDate) 
    {
        this.dueDate = dueDate;
    }

    public Date getDueDate() 
    {
        return dueDate;
    }
    public void setReturnDate(Date returnDate) 
    {
        this.returnDate = returnDate;
    }

    public Date getReturnDate() 
    {
        return returnDate;
    }
    public void setFine(BigDecimal fine) 
    {
        this.fine = fine;
    }

    public BigDecimal getFine() 
    {
        return fine;
    }
    public void setComments(String comments) 
    {
        this.comments = comments;
    }

    public String getComments() 
    {
        return comments;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("borrowId", getBorrowId())
            .append("bookId", getBookId())
            .append("readerId", getReaderId())
            .append("libraryId", getLibraryId())
            .append("borrowDate", getBorrowDate())
            .append("dueDate", getDueDate())
            .append("returnDate", getReturnDate())
            .append("fine", getFine())
            .append("comments", getComments())
            .toString();
    }
}
