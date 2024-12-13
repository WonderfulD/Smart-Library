package com.ruoyi.book.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 图书副本信息对象 Books
 *
 * @author ruoyi
 * @date 2024-03-12
 */
public class Books extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 图书ID
     */
    @Excel(name = "图书ID")
    private Long bookId;

    /**
     * 书名
     */
    @Excel(name = "书名")
    private String title;

    /**
     * 作者
     */
    @Excel(name = "作者")
    private String author;

    /**
     * 国际标准书号
     */
    @Excel(name = "国际标准书号")
    private String isbn;

    /**
     * 出版社
     */
    @Excel(name = "出版社")
    private String publisher;

    /**
     * 出版日期
     */
    private Date publishDate;

    /**
     * 图书分类
     */
    @Excel(name = "图书分类")
    private String category;

    /**
     * 图书描述
     */
    @Excel(name = "图书描述")
    private String description;

    /**
     * 图书语言
     */
    private String language;

    /**
     * 页数
     */
    private Long pages;

    /**
     * 封面图片URL
     */
    private String coverUrl;

    /**
     * 版次
     */
    @Excel(name = "版次")
    private String edition;


    /**
     * 简介
     */
    @Excel(name = "简介")
    private String summary;

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Long getPages() {
        return pages;
    }

    public void setPages(Long pages) {
        this.pages = pages;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public String toString() {
        return "Books{" +
                "bookId=" + bookId +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", isbn='" + isbn + '\'' +
                ", publisher='" + publisher + '\'' +
                ", publishDate=" + publishDate +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", language='" + language + '\'' +
                ", pages=" + pages +
                ", coverUrl='" + coverUrl + '\'' +
                ", edition='" + edition + '\'' +
                ", summary='" + summary + '\'' +
                '}';
    }
}
