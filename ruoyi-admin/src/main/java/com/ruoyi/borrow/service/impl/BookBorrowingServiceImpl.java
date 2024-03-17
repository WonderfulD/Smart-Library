package com.ruoyi.borrow.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.borrow.mapper.BookBorrowingMapper;
import com.ruoyi.borrow.domain.BookBorrowing;
import com.ruoyi.borrow.service.IBookBorrowingService;

/**
 * 图书借阅信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-03-12
 */
@Service
public class BookBorrowingServiceImpl implements IBookBorrowingService 
{
    @Autowired
    private BookBorrowingMapper bookBorrowingMapper;

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
}
