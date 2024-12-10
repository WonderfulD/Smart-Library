package com.ruoyi.book.service;

import com.ruoyi.book.domain.Books;
import com.ruoyi.book.domain.BooksBO;
import com.ruoyi.common.core.domain.AjaxResult;

import java.util.List;

/**
 * 图书副本信息Service接口
 *
 * @author ruoyi
 * @date 2024-03-12
 */
public interface IBooksService
{
    /**
     * 查询图书副本信息
     *
     * @param bookId 图书副本信息主键
     * @return 图书副本信息
     */
    public Books selectBooksByBookId(Long bookId);

    /**
     * 查询图书副本信息列表
     *
     * @param books 图书副本信息
     * @return 图书副本信息集合
     */
    public List<Books> selectBooksList(Books books);

    /**
     * 新增图书副本信息
     *
     * @param books 图书副本信息
     * @return 结果
     */
    public int insertBooks(Books books);

    /**
     * 修改图书副本信息
     *
     * @param books 图书副本信息
     * @return 结果
     */
    public int updateBooks(Books books);

    /**
     * 批量删除图书副本信息
     *
     * @param bookIds 需要删除的图书副本信息主键集合
     * @return 结果
     */
    public int deleteBooksByBookIds(Long[] bookIds);

    /**
     * 删除图书副本信息信息
     *
     * @param bookId 图书副本信息主键
     * @return 结果
     */
    public int deleteBooksByBookId(Long bookId);

    /**
     * 根据图书馆ID查询图书副本信息列表
     */
    public List<Books> selectBooksListByLibrary(Books books);

    public List<Books> selectAvailableBooksList(List<Integer> availableBookIds);

    /**
     * 新增图书，插入Books表和book_storage表
     */
    public AjaxResult addBooks(BooksBO booksBO);

    public AjaxResult removeBooks(Long[] bookIds);
}
