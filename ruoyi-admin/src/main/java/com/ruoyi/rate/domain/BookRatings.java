package com.ruoyi.rate.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 藏书评分对象 book_ratings
 * 
 * @author ruoyi
 * @date 2024-03-20
 */
public class BookRatings extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 评分编号 */
    @Excel(name = "评分编号")
    private Long ratingId;

    /** 图书编号 */
    @Excel(name = "图书编号")
    private Long bookId;

    /** 读者号 */
    @Excel(name = "读者号")
    private Long readerId;

    /** 评分 */
    @Excel(name = "评分")
    private Long rating;

    /** 评论 */
    @Excel(name = "评论")
    private String comment;

    /** 评分时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "评分时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date ratingTime;

    public void setRatingId(Long ratingId) 
    {
        this.ratingId = ratingId;
    }

    public Long getRatingId() 
    {
        return ratingId;
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
    public void setRating(Long rating) 
    {
        this.rating = rating;
    }

    public Long getRating() 
    {
        return rating;
    }
    public void setComment(String comment) 
    {
        this.comment = comment;
    }

    public String getComment() 
    {
        return comment;
    }
    public void setRatingTime(Date ratingTime) 
    {
        this.ratingTime = ratingTime;
    }

    public Date getRatingTime() 
    {
        return ratingTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("ratingId", getRatingId())
            .append("bookId", getBookId())
            .append("readerId", getReaderId())
            .append("rating", getRating())
            .append("comment", getComment())
            .append("ratingTime", getRatingTime())
            .toString();
    }
}
