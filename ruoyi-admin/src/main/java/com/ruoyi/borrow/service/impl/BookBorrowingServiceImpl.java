package com.ruoyi.borrow.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import com.ruoyi.Utils.BorrowUtil;
import com.ruoyi.book.domain.Books;
import com.ruoyi.book.service.IBooksService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.storage.domain.BookStorage;
import com.ruoyi.storage.service.IBookStorageService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.borrow.mapper.BookBorrowingMapper;
import com.ruoyi.borrow.domain.BookBorrowing;
import com.ruoyi.borrow.service.IBookBorrowingService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import static com.ruoyi.RedisConstants.BOOK_BORROW_LOCK;

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

    @Autowired
    private IBookStorageService bookStorageService;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private IBookBorrowingService bookBorrowingService;

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
    @Transactional
    public AjaxResult handleExtension(BookBorrowing request) {
        try {
            Long bookId = request.getBookId();

            // 先检查图书是否存在
            Books book = booksService.selectBooksByBookId(bookId);
            if (book == null) {
                return AjaxResult.error("图书不存在");
            }
            // 检查图书是否已归还
            BookBorrowing checkBookBorrowing = selectBookBorrowingByBorrowId(request.getBorrowId());
            if (checkBookBorrowing.getReturnDate() != null) {
                return AjaxResult.error("操作失败，图书已归还");
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

    /**
     * 处理图书借阅
     * 乐观锁解决超借
     * Redisson锁解决“读者无法在未归还某图书前再次借阅该图书”
     * @param bookBorrowing
     * @return
     */
    @Override
    public AjaxResult borrowBook(BookBorrowing bookBorrowing) {
        Long bookId = bookBorrowing.getBookId();
        Long readerId = bookBorrowing.getReaderId();

        //获取Redisson分布式锁
        String lockKey = BOOK_BORROW_LOCK + ":" + readerId + ":" + bookId;
        RLock lock = redissonClient.getLock(lockKey);

        //尝试获取锁
        boolean locked = lock.tryLock();

        if (!locked) {
            //未获取到锁
            return AjaxResult.error("无法借阅，有一本相同图书未归还");
        }
        try {
            // 先检查图书是否存在
            Books book = booksService.selectBooksByBookId(bookId);
            if (book == null) {
                return AjaxResult.error("图书不存在");
            }
            //检查图书是否还有存货
            Long bookTotalStock = bookStorageService.selectTotalStockByBookId(bookId);
            if (bookTotalStock < 1L) {
                //没有存货了
                return AjaxResult.error("所选图书已全部借出");
            }
            //获取了锁
            return bookBorrowingService.makeBorrow(bookBorrowing, bookId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            //释放锁
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    /**
     * 借阅流程
     * @param bookBorrowing
     * @param bookId
     * @return
     */
    @Override
    @Transactional
    public AjaxResult makeBorrow(BookBorrowing bookBorrowing, Long bookId) {
        //判断是否已经有一本相同图书未归还
        Long readerId = bookBorrowing.getReaderId();
        Long notReturnedBooksCount = bookBorrowingMapper.selectNotReturnedBooksCountByReaderIdAndBookId(readerId, bookId);

        if (notReturnedBooksCount > 0L) {
            //已经有一本相同图书未归还
            return AjaxResult.error("无法借阅，有一本相同图书未归还");
        }

        //没有同一本尚未归还的图书
        //随机选择一家图书馆
        List<Long> libraryIds = bookStorageService.selectLibraryIdsByBookId(bookId);
        int randomIndex = ThreadLocalRandom.current().nextInt(libraryIds.size());
        Long libraryId = libraryIds.get(randomIndex);

        //减去库存，乐观锁，判断库存大于0时才减去库存
        Boolean updated = bookStorageService.borrowOneBookWithOLock(libraryId, bookId);

        if (!updated) {
            //减库存不成功，已经卖完
            return AjaxResult.error("所选图书已全部借出");
        }

        // 创建并保存借阅记录
        LocalDate borrowDate = bookBorrowing.getBorrowDate();
        Long borrowMethod = bookBorrowing.getBorrowMethod();
        Long borrowId = BorrowUtil.generateBorrowId();
        BookBorrowing insertBookBorrowing = new BookBorrowing();
        insertBookBorrowing.setBookId(bookId);
        insertBookBorrowing.setLibraryId(libraryId);
        insertBookBorrowing.setReaderId(readerId);
        insertBookBorrowing.setBorrowDate(borrowDate);
        insertBookBorrowing.setBorrowMethod(borrowMethod);
        insertBookBorrowing.setBorrowId(borrowId);
        //设置借阅状态待审核
        insertBookBorrowing.setPendingStatus(2L);
        //设置借阅备注
        String comments = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = borrowDate.format(formatter);
        if (borrowMethod == 0L) {  //到馆
            comments = "到馆取阅, 取阅时间为: " + formattedDate;
        } else if (borrowMethod == 1L) {
            comments = "邮寄取阅, 邮寄地址为: " + insertBookBorrowing.getComments();

        }
        insertBookBorrowing.setComments(comments);
        insertBookBorrowing(insertBookBorrowing);

        return AjaxResult.success("借阅成功，待审核");
    }


    /**
     * 根据当前登录管理员所在图书馆ID，查询读者归还待确认的借阅列表
     * @param bookBorrowing
     * @return
     */
    @Override
    public List<BookBorrowing> selectBookBorrowingListWithReturnPending(BookBorrowing bookBorrowing) {
        return bookBorrowingMapper.selectBookBorrowingListWithReturnPending(bookBorrowing);
    }

    /**
     * 处理读者图书归还
     * @param request 借阅记录
     * @return AjaxResult 响应结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult returnBook(BookBorrowing request) {
        try {
            Long bookId = request.getBookId();
            // 先检查图书是否存在
            Books book = booksService.selectBooksByBookId(bookId);
            if (book == null) {
                return AjaxResult.error("操作失败，图书不存在");
            }

            // 检查图书是否已归还
            BookBorrowing borrowing = selectBookBorrowingByBorrowId(request.getBorrowId());
            if (borrowing.getReturnDate() != null) {
                return AjaxResult.error("操作失败，图书已归还");
            }

            // 创建并修改借阅记录
            BookBorrowing bookBorrowing = new BookBorrowing();
            Long borrowId = request.getBorrowId();
            LocalDate returnDate = request.getReturnDate();
            Long returnMethod = request.getReturnMethod();
            String comment;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = returnDate.format(formatter);
            if (returnMethod == 1L) { // 邮寄归还
                String trackingNumber = request.getTrackingNumber();
                comment = "邮寄归还, 寄出时间为: " + formattedDate;
                bookBorrowing.setTrackingNumber(trackingNumber);
            } else { // 到馆归还
                comment = "到馆归还, 预约时间为: " + formattedDate;
            }
            bookBorrowing.setBookId(bookId);
            bookBorrowing.setBorrowId(borrowId);
            bookBorrowing.setComments(comment);
            bookBorrowing.setReturnMethod(returnMethod);
            updateBookBorrowing(bookBorrowing);
            return AjaxResult.success("已提交，您的预计归还日期为:" + formattedDate);
        } catch (Exception e) {
            log.info("图书归还失败，回滚\n错误信息{}", e.getMessage());
            // 手动设置事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return AjaxResult.error("归还失败，请稍后再试");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult confirmReturnBook(BookBorrowing request) {
        try {
            Long bookId = request.getBookId();
            // 先检查图书是否存在
            Books book = booksService.selectBooksByBookId(bookId);
            if (book == null) {
                return AjaxResult.error("操作失败，图书不存在");
            }

            // 检查图书是否已归还
            BookBorrowing borrowing = selectBookBorrowingByBorrowId(request.getBorrowId());
            if (borrowing.getReturnDate() != null) {
                return AjaxResult.error("操作失败，图书已归还");
            }

            Long borrowId = request.getBorrowId();
            LocalDate dueDate = request.getDueDate();
            LocalDate returnDate = LocalDate.now();
            String comment = "";
            Long fine = 0L;
            if (returnDate.isBefore(dueDate)) { //按时归还
                comment += "按时归还";
            } else { //逾期
                comment += "逾期";
                //计算逾期天数
                long overdueDays = returnDate.compareTo(dueDate);
                fine = BorrowUtil.calculateFine(overdueDays);
            }

            // 创建并修改借阅记录
            BookBorrowing bookBorrowing = new BookBorrowing();
            bookBorrowing.setBookId(bookId);
            bookBorrowing.setBorrowId(borrowId);
            bookBorrowing.setReturnDate(returnDate);
            bookBorrowing.setComments(comment);
            bookBorrowing.setFine(BigDecimal.valueOf(fine));
            updateBookBorrowing(bookBorrowing);

            //增加库存
            BookStorage bookStorage = new BookStorage();
            bookStorage.setBookId(bookId);
            bookStorage.setLibraryId(request.getLibraryId());
            List<BookStorage> bookStorageList = bookStorageService.selectBookStorageList(bookStorage);
            BookStorage updateBookStorage = bookStorageList.get(0);
            Long stock = updateBookStorage.getStock();
            updateBookStorage.setStock(stock + 1);
            bookStorageService.updateBookStorage(updateBookStorage);

            return AjaxResult.success("图书已确认归还");
        } catch (Exception e) {
            log.info("确认图书归还失败，回滚\n错误信息{}", e.getMessage());
            // 手动设置事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return AjaxResult.error("确认归还失败，请稍后再试");
        }
    }
}
