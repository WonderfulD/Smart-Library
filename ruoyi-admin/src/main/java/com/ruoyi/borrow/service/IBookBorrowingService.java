package com.ruoyi.borrow.service;

import java.util.Date;
import java.util.List;

import com.ruoyi.book.domain.Books;
import com.ruoyi.borrow.domain.BookBorrowing;
import com.ruoyi.common.core.domain.AjaxResult;

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
    public List<BookBorrowing> selectBookBorrowingListByDept(BookBorrowing bookBorrowing);

    /**
     * 根据借阅人ID查询图书借阅信息
     *
     * @param bookBorrowing 包含查询条件的实体，包括借阅人ID
     * @return 符合条件的图书借阅信息列表
     */
    public List<BookBorrowing> selectBookBorrowingListByReaderId(BookBorrowing bookBorrowing);


    /**
     * 根据借阅日期查询截止借阅日期的累计会员数
     *
     * @param date 借阅日期
     * @return 符合条件的会员数
     */
    public Integer countDistinctReaderIdsByDate(Date date, Long libraryId);


    /**
     * 查询已通过审核未还的书籍借阅列表
     *
     * @param bookBorrowing 包含查询条件的实体，包括借阅审核情况
     * @return 符合条件的图书借阅信息列表
     */
    public List<BookBorrowing> selectBookBorrowingByPendingStatusWithNullReturnDate(BookBorrowing bookBorrowing);

    /**
     * 根据当前登录管理员所在图书馆ID，查询每天借阅图书所属种类列表
     *
     * @param bookBorrowing 包含查询条件的实体，包括图书馆Id
     * @return 符合条件的图书借阅信息列表，包括图书种类
     */
    public List<BookBorrowing> selectBookBorrowingListByLibraryIdWithCategory(BookBorrowing bookBorrowing);

    /**
     * 处理借阅延期
     * @param request
     * @return 响应结果
     */
    public AjaxResult handleExtension(BookBorrowing request);

    /**
     * 处理图书借阅
     * @param bookBorrowing
     * @return
     */
    public AjaxResult borrowBook(BookBorrowing bookBorrowing);

    /**
     * 根据当前登录管理员所在图书馆ID，查询读者归还待确认的借阅列表
     * @param bookBorrowing
     * @return
     */
    public List<BookBorrowing> selectBookBorrowingListWithReturnPending(BookBorrowing bookBorrowing);

    /**
     * 处理读者图书归还
     * @param request 借阅记录
     * @return AjaxResult 响应结果
     */
    public AjaxResult returnBook(BookBorrowing request);

    public AjaxResult confirmReturnBook(BookBorrowing request);

    /**
     * 借阅流程
     * @param bookBorrowing
     * @param bookId
     * @return
     */
    public AjaxResult makeBorrow(BookBorrowing bookBorrowing, Long bookId);
}
