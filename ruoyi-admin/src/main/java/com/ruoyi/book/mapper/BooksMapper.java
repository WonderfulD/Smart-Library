package com.ruoyi.book.mapper;

import java.util.List;
import com.ruoyi.book.domain.Books;

/**
 * 图书副本信息Mapper接口
 * 
 * @author ruoyi
 * @date 2024-03-12
 */
public interface BooksMapper 
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
     * 删除图书副本信息
     * 
     * @param bookId 图书副本信息主键
     * @return 结果
     */
    public int deleteBooksByBookId(Long bookId);

    /**
     * 批量删除图书副本信息
     * 
     * @param bookIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBooksByBookIds(Long[] bookIds);

    /**
     * 根据图书馆ID查询图书副本信息列表
     */
    List<Books> selectBooksListByLibrary(Books books);

}
