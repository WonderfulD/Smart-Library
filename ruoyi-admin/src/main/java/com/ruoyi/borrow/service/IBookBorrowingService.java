package com.ruoyi.borrow.service;

import java.util.Date;
import java.util.List;

import com.ruoyi.book.domain.Books;
import com.ruoyi.borrow.domain.BookBorrowing;

/**
 * 图书借阅信息Service接口
 * 
 * @author ruoyi
 * @date 2024-03-12
 */
public interface IBookBorrowingService 
{
    /**
     * 查询图书借阅信息
     * 
     * @param borrowId 图书借阅信息主键
     * @return 图书借阅信息
     */
    public BookBorrowing selectBookBorrowingByBorrowId(Long borrowId);

    /**
     * 查询图书借阅信息列表
     * 
     * @param bookBorrowing 图书借阅信息
     * @return 图书借阅信息集合
     */
    public List<BookBorrowing> selectBookBorrowingList(BookBorrowing bookBorrowing);

    /**
     * 新增图书借阅信息
     * 
     * @param bookBorrowing 图书借阅信息
     * @return 结果
     */
    public int insertBookBorrowing(BookBorrowing bookBorrowing);

    /**
     * 修改图书借阅信息
     * 
     * @param bookBorrowing 图书借阅信息
     * @return 结果
     */
    public int updateBookBorrowing(BookBorrowing bookBorrowing);

    /**
     * 批量删除图书借阅信息
     * 
     * @param borrowIds 需要删除的图书借阅信息主键集合
     * @return 结果
     */
    public int deleteBookBorrowingByBorrowIds(Long[] borrowIds);

    /**
     * 删除图书借阅信息信息
     * 
     * @param borrowId 图书借阅信息主键
     * @return 结果
     */
    public int deleteBookBorrowingByBorrowId(Long borrowId);


    /**
     * 根据部门查询图书借阅信息
     *
     * @param bookBorrowing 包含查询条件的实体，包括图书馆（部门）ID
     * @return 符合条件的图书借阅信息列表
     */
    List<BookBorrowing> selectBookBorrowingListByDept(BookBorrowing bookBorrowing);

    /**
     * 根据借阅人ID查询图书借阅信息
     *
     * @param bookBorrowing 包含查询条件的实体，包括借阅人ID
     * @return 符合条件的图书借阅信息列表
     */
    List<BookBorrowing> selectBookBorrowingListByReaderId(BookBorrowing bookBorrowing);



    /**
     * 根据借阅日期查询截止借阅日期的累计会员数
     *
     * @param date 借阅日期
     * @return 符合条件的会员数
     */
    Integer countDistinctReaderIdsByDate(Date date);
}
