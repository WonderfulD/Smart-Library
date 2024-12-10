package com.ruoyi.book.domain;


import com.ruoyi.common.core.domain.BaseEntity;

import java.util.Date;


public class BooksBO extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 图书ID
     */
    private Long bookId;

    /**
     * 图书馆ID
     */
    private Long libraryId;

    /**
     * 书名
     */
    private String title;

    /**
     * 作者
     */
    private String author;

    /**
     * 国际标准书号
     */
    private String isbn;

    /**
     * 出版社
     */
    private String publisher;

    /**
     * 出版日期
     */
    private Date publishDate;

    /**
     * 图书分类
     */
    private String category;

    /**
     * 图书描述
     */
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
    private String edition;

    /**
     * 借阅状态
     */
    private Long status;

    /**
     * 简介
     */
    private String summary;

    private Long amount;

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(Long libraryId) {
        this.libraryId = libraryId;
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

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "BooksStorage{" +
                "bookId=" + bookId +
                ", libraryId=" + libraryId +
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
                ", status=" + status +
                ", summary='" + summary + '\'' +
                ", amount=" + amount +
                '}';
    }
}

