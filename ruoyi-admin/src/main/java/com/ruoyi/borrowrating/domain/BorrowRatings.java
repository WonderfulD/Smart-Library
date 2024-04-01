package com.ruoyi.borrowrating.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 借阅评分对象 borrow_ratings
 *
 * @author ruoyi
 * @date 2024-03-30
 */
public class BorrowRatings extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 借阅号
     */
    @Excel(name = "借阅号")
    private Long borrowId;

    /**
     * 书本编号
     */
    @Excel(name = "书本编号")
    private Long bookId;

    /**
     * 读者号
     */
    @Excel(name = "读者号")
    private Long readerId;

    /**
     * 图书馆编号
     */
    @Excel(name = "图书馆编号")
    private Long libraryId;

    /**
     * 了解渠道
     */
    @Excel(name = "了解渠道")
    private Long discoveryChannel;

    /**
     * 选择理由
     */
    @Excel(name = "选择理由")
    private String selectionReasons;

    /**
     * 借阅满意度
     */
    @Excel(name = "借阅满意度")
    private Long borrowingSatisfaction;

    /**
     * 图书质量满意度
     */
    @Excel(name = "图书质量满意度")
    private Long bookQualitySatisfaction;

    /**
     * 图书馆满意度
     */
    @Excel(name = "图书馆满意度")
    private Long librarySatisfaction;

    /**
     * 推荐意愿
     */
    @Excel(name = "推荐意愿")
    private Long recommendationWillingness;

    /**
     * 读者建议
     */
    @Excel(name = "读者建议")
    private String suggestions;
    /**
     * 评分时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "评分时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date ratingDate;

    public Date getRatingDate() {
        return ratingDate;
    }

    public void setRatingDate(Date ratingDate) {
        this.ratingDate = ratingDate;
    }

    public Long getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(Long borrowId) {
        this.borrowId = borrowId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getReaderId() {
        return readerId;
    }

    public void setReaderId(Long readerId) {
        this.readerId = readerId;
    }

    public Long getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(Long libraryId) {
        this.libraryId = libraryId;
    }

    public Long getDiscoveryChannel() {
        return discoveryChannel;
    }

    public void setDiscoveryChannel(Long discoveryChannel) {
        this.discoveryChannel = discoveryChannel;
    }

    public String getSelectionReasons() {
        return selectionReasons;
    }

    public void setSelectionReasons(String selectionReasons) {
        this.selectionReasons = selectionReasons;
    }

    public Long getBorrowingSatisfaction() {
        return borrowingSatisfaction;
    }

    public void setBorrowingSatisfaction(Long borrowingSatisfaction) {
        this.borrowingSatisfaction = borrowingSatisfaction;
    }

    public Long getBookQualitySatisfaction() {
        return bookQualitySatisfaction;
    }

    public void setBookQualitySatisfaction(Long bookQualitySatisfaction) {
        this.bookQualitySatisfaction = bookQualitySatisfaction;
    }

    public Long getLibrarySatisfaction() {
        return librarySatisfaction;
    }

    public void setLibrarySatisfaction(Long librarySatisfaction) {
        this.librarySatisfaction = librarySatisfaction;
    }

    public Long getRecommendationWillingness() {
        return recommendationWillingness;
    }

    public void setRecommendationWillingness(Long recommendationWillingness) {
        this.recommendationWillingness = recommendationWillingness;
    }

    public String getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(String suggestions) {
        this.suggestions = suggestions;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("borrowId", getBorrowId())
                .append("bookId", getBookId())
                .append("readerId", getReaderId())
                .append("libraryId", getLibraryId())
                .append("discoveryChannel", getDiscoveryChannel())
                .append("selectionReasons", getSelectionReasons())
                .append("borrowingSatisfaction", getBorrowingSatisfaction())
                .append("bookQualitySatisfaction", getBookQualitySatisfaction())
                .append("librarySatisfaction", getLibrarySatisfaction())
                .append("recommendationWillingness", getRecommendationWillingness())
                .append("suggestions", getSuggestions())
                .append("ratingDate", getRatingDate())
                .toString();
    }
}
