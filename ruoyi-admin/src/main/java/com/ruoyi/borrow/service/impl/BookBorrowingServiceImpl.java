package com.ruoyi.borrow.service.impl;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.ruoyi.book.domain.Books;
import com.ruoyi.book.service.IBooksService;
import com.ruoyi.common.core.domain.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.borrow.mapper.BookBorrowingMapper;
import com.ruoyi.borrow.domain.BookBorrowing;
import com.ruoyi.borrow.service.IBookBorrowingService;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * 图书借阅信息Service业务层处理
 *
 * @author ruoyi
 * @date 2024-03-12
 */
@Service
@Slf4j
public class BookBorrowingServiceImpl implements IBookBorrowingService
{
    @Autowired
    private BookBorrowingMapper bookBorrowingMapper;

    @Autowired
    private IBooksService booksService;

    /**
     * 查询图书借阅信息
     *
     * @param borrowId 图书借阅信息主键
     * @return 图书借阅信息
     */
    @Override
    public BookBorrowing selectBookBorrowingByBorrowId(Long borrowId)
    {
        return bookBorrowingMapper.selectBookBorrowingByBorrowId(borrowId);
    }

    /**
     * 查询图书借阅信息列表
     *
     * @param bookBorrowing 图书借阅信息
     * @return 图书借阅信息
     */
    @Override
    public List<BookBorrowing> selectBookBorrowingList(BookBorrowing bookBorrowing)
    {
        return bookBorrowingMapper.selectBookBorrowingList(bookBorrowing);
    }

    /**
     * 新增图书借阅信息
     *
     * @param bookBorrowing 图书借阅信息
     * @return 结果
     */
    @Override
    public int insertBookBorrowing(BookBorrowing bookBorrowing)
    {
        return bookBorrowingMapper.insertBookBorrowing(bookBorrowing);
    }

    /**
     * 修改图书借阅信息
     *
     * @param bookBorrowing 图书借阅信息
     * @return 结果
     */
    @Override
    public int updateBookBorrowing(BookBorrowing bookBorrowing)
    {
        return bookBorrowingMapper.updateBookBorrowing(bookBorrowing);
    }

    /**
     * 批量删除图书借阅信息
     *
     * @param borrowIds 需要删除的图书借阅信息主键
     * @return 结果
     */
    @Override
    public int deleteBookBorrowingByBorrowIds(Long[] borrowIds)
    {
        return bookBorrowingMapper.deleteBookBorrowingByBorrowIds(borrowIds);
    }

    /**
     * 删除图书借阅信息信息
     *
     * @param borrowId 图书借阅信息主键
     * @return 结果
     */
    @Override
    public int deleteBookBorrowingByBorrowId(Long borrowId)
    {
        return bookBorrowingMapper.deleteBookBorrowingByBorrowId(borrowId);
    }


    /**
     * 根据部门查询图书借阅信息
     *
     * @param bookBorrowing 包含查询条件的实体，包括图书馆（部门）ID
     * @return 符合条件的图书借阅信息列表
     */
    @Override
    public List<BookBorrowing> selectBookBorrowingListByDept(BookBorrowing bookBorrowing) {
        // 调用Mapper层的方法，传入BookBorrowing对象，该对象包含了部门ID
        return bookBorrowingMapper.selectBookBorrowingListByDept(bookBorrowing);
    }


    /**
     * 根据借阅人ID查询图书借阅信息
     *
     * @param bookBorrowing 包含查询条件的实体，包括借阅人ID
     * @return 符合条件的图书借阅信息列表
     */
    @Override
    public List<BookBorrowing> selectBookBorrowingListByReaderId(BookBorrowing bookBorrowing) {
        return bookBorrowingMapper.selectBookBorrowingListByReaderId(bookBorrowing);
    }

    /**
     * 根据借阅日期查询截止借阅日期的累计会员数
     *
     * @param date 借阅日期
     * @return 符合条件的会员数
     */
    @Override
    public Integer countDistinctReaderIdsByDate(Date date, Long libraryId) {
        return bookBorrowingMapper.countDistinctReaderIdsByDate(date, libraryId);
    }



    /**
     * 查询已通过审核未还的书籍借阅列表
     *
     * @param bookBorrowing 包含查询条件的实体，包括节约审核情况
     * @return 符合条件的图书借阅信息列表
     */
    @Override
    public List<BookBorrowing> selectBookBorrowingByPendingStatusWithNullReturnDate(BookBorrowing bookBorrowing) {
        return bookBorrowingMapper.selectBookBorrowingByPendingStatusWithNullReturnDate(bookBorrowing);
    }

    /**
     * 根据当前登录管理员所在图书馆ID，查询每天借阅图书所属种类列表
     *
     * @param bookBorrowing 包含查询条件的实体，包括图书馆Id
     * @return 符合条件的图书借阅信息列表，包括图书种类
     */
    @Override
    public List<BookBorrowing> selectBookBorrowingListByLibraryIdWithCategory(BookBorrowing bookBorrowing) {
        return bookBorrowingMapper.selectBookBorrowingListByLibraryIdWithCategory(bookBorrowing);
    }

    /**
     * 处理借阅延期
     * @param request
     * @return 响应结果
     */
    @Override
    public AjaxResult handleExtension(BookBorrowing request) {
        try {
            Long bookId = request.getBookId();
            // 先检查图书是否存在
            Books book = booksService.selectBooksByBookId(bookId);
            if (book == null) {
                return AjaxResult.error("图书不存在");
            }
            // 检查图书是否已归还
            if (book.getStatus() == 1) {
                return AjaxResult.error("图书已归还");
            }else if (book.getStatus() == 2) {
                return AjaxResult.error("当前无法完成此操作");
            }
            Long borrowId = request.getBorrowId();
            //检查是否已逾期
            LocalDate dueDate = request.getDueDate();
            if (dueDate.isBefore(LocalDate.now())) {
                return AjaxResult.error("不允许延期，你已经逾期！");
            }
            //检查是否已有延期
            BookBorrowing borrowing = selectBookBorrowingByBorrowId(borrowId);
            if (borrowing.getComments().equals("延期15天成功")) {
                return AjaxResult.error("不允许延期，你已经申请过延期！");
            }
            LocalDate extensionDate = dueDate.plusDays(15);
            // 创建并修改借阅记录
            BookBorrowing bookBorrowing = new BookBorrowing();
            bookBorrowing.setBookId(bookId);
            bookBorrowing.setBorrowId(borrowId);
            bookBorrowing.setDueDate(extensionDate);
            bookBorrowing.setComments("延期15天成功");
            updateBookBorrowing(bookBorrowing);
            return AjaxResult.success("延期成功");
        } catch (Exception e) {
            log.info("借阅延期失败，回滚\n错误信息{}", e.getMessage());
            // 手动设置事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return AjaxResult.error("延期失败，请稍后再试");
        }
    }
}
