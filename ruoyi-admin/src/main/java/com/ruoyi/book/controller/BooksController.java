package com.ruoyi.book.controller;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.book.domain.Books;
import com.ruoyi.book.service.IBooksService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.ruoyi.borrow.domain.BookBorrowing;
import com.ruoyi.borrow.service.IBookBorrowingService;

/**
 * 图书副本信息Controller
 * 
 * @author ruoyi
 * @date 2024-03-12
 */
@RestController
@RequestMapping("/book/BookInfo")
public class BooksController extends BaseController
{
    @Autowired
    private IBooksService booksService;

    @Autowired
    private IBookBorrowingService bookBorrowingService; // 自动注入借阅服务

    /**
     * 查询图书副本信息列表
     */
//    @PreAuthorize("@ss.hasPermi('book:BookInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(Books books)
    {
        startPage();
        List<Books> list = booksService.selectBooksList(books);
        return getDataTable(list);
    }

    /**
     * 根据图书馆ID查询图书副本信息列表
     */
//    @PreAuthorize("@ss.hasPermi('book:BookInfo:list')")
    @GetMapping("/listByLibrary")
    public TableDataInfo listByLibrary(Books books)
    {
        startPage();
        books.setLibraryId(SecurityUtils.getDeptId()); // 设置当前用户所在部门ID
        List<Books> list = booksService.selectBooksListByLibrary(books);
        return getDataTable(list);
    }

    /**
     * 根据借阅人ID查询图书副本信息列表
     */
//    @PreAuthorize("@ss.hasPermi('book:BookInfo:list')")
    @GetMapping("/listByReader")
    public TableDataInfo listByReader(BookBorrowing bookBorrowing)
    {
        startPage();
        bookBorrowing.setReaderId(SecurityUtils.getUserId()); // 设置当前用户ID
        List<Books> list = bookBorrowingService.selectBookBorrowingListByReaderId(bookBorrowing);
        return getDataTable(list);
    }


    /**
     * 导出图书副本信息列表
     */
//    @PreAuthorize("@ss.hasPermi('book:BookInfo:export')")
    @Log(title = "图书副本信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Books books)
    {
        List<Books> list = booksService.selectBooksList(books);
        ExcelUtil<Books> util = new ExcelUtil<Books>(Books.class);
        util.exportExcel(response, list, "图书副本信息数据");
    }

    /**
     * 获取图书副本信息详细信息
     */
//    @PreAuthorize("@ss.hasPermi('book:BookInfo:query')")
    @GetMapping(value = "/{bookId}")
    public AjaxResult getInfo(@PathVariable("bookId") Long bookId)
    {
        return success(booksService.selectBooksByBookId(bookId));
    }

    /**
     * 新增图书副本信息
     */
//    @PreAuthorize("@ss.hasPermi('book:BookInfo:add')")
    @Log(title = "图书副本信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Books books)
    {
        return toAjax(booksService.insertBooks(books));
    }

    /**
     * 修改图书副本信息
     */
//    @PreAuthorize("@ss.hasPermi('book:BookInfo:edit')")
    @Log(title = "图书副本信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Books books)
    {
        return toAjax(booksService.updateBooks(books));
    }

    /**
     * 删除图书副本信息
     */
//    @PreAuthorize("@ss.hasPermi('book:BookInfo:remove')")
    @Log(title = "图书副本信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{bookIds}")
    public AjaxResult remove(@PathVariable Long[] bookIds)
    {
        return toAjax(booksService.deleteBooksByBookIds(bookIds));
    }

    /**
     * 处理图书借阅
     * @return AjaxResult 响应结果
     */
    @Transactional
    @PostMapping("/borrow")
    public AjaxResult handleBorrow(@RequestBody BookBorrowing request) {
        try {
            Long bookId = request.getBookId();
            // 先检查图书是否存在
            Books book = booksService.selectBooksByBookId(bookId);
            if (book == null) {
                return AjaxResult.error("图书不存在");
            }
            // 检查图书是否已借出
            if (book.getStatus() == 1) {
                return AjaxResult.error("图书已被借出");
            }
            // 更新图书状态为借出
            book.setStatus(1L);
            booksService.updateBooks(book);
            Long readerId = request.getReaderId();
            Long libraryId = request.getLibraryId();
            Date borrowDate = request.getBorrowDate();
            Date dueDate = request.getDueDate();
            // 创建并保存借阅记录
            BookBorrowing bookBorrowing = new BookBorrowing();
            bookBorrowing.setBookId(bookId);
            bookBorrowing.setReaderId(readerId);
            bookBorrowing.setLibraryId(libraryId);
            bookBorrowing.setBorrowDate(borrowDate);
            bookBorrowing.setDueDate(dueDate);
            bookBorrowingService.insertBookBorrowing(bookBorrowing);

            return AjaxResult.success("借阅成功");
        } catch (Exception e) {
            // 如果有任何异常，Spring会回滚事务
            e.printStackTrace();
            return AjaxResult.error("借阅失败，请稍后再试");
        }
    }


    /**
     * 处理图书归还
     * @return AjaxResult 响应结果
     */
    @Transactional
    @PostMapping("/return")
    public AjaxResult handleReturn(@RequestBody BookBorrowing request) {
        try {
            Long bookId = request.getBookId();
            // 先检查图书是否存在
            Books book = booksService.selectBooksByBookId(bookId);
            if (book == null) {
                return AjaxResult.error("图书不存在");
            }
            // 检查图书是否已归还
            if (book.getStatus() == 0) {
                return AjaxResult.error("图书已归还");
            }
            // 更新图书状态为已归还
            book.setStatus(0L);
            booksService.updateBooks(book);
            Long borrowId = request.getBorrowId();
            Date dueDate = request.getDueDate();
            Date returnDate = request.getReturnDate();
            String comment = "";
            Long fine = 0L;
            if (returnDate.before(dueDate)) { //按时归还
                comment += "按时归还";
            } else { //逾期 每天逾期罚款0.3元,最高不超过30元,逾期超过100天按最高30元计算。
                comment += "逾期";
                //计算逾期天数
                long overdueDays = (returnDate.getTime() - dueDate.getTime()) / (24 * 60 * 60 * 1000);
                //计算罚款
                if (overdueDays <= 100) { //封顶100天
                    fine = Math.min(overdueDays * 3, 30L) * 10; //每天0.3元,封顶30元
                } else {
                    fine = 300L; //超过100天,直接上限300元
                }
            }
            // 创建并修改借阅记录
            BookBorrowing bookBorrowing = new BookBorrowing();
            bookBorrowing.setBookId(bookId);
            bookBorrowing.setBorrowId(borrowId);
            bookBorrowing.setReturnDate(returnDate);
            bookBorrowing.setComments(comment);
            System.out.println("\n" + bookBorrowing);
            bookBorrowingService.updateBookBorrowing(bookBorrowing);

            return AjaxResult.success("归还成功");
        } catch (Exception e) {
            // 如果有任何异常，Spring会回滚事务
            e.printStackTrace();
            return AjaxResult.error("归还失败，请稍后再试");
        }
    }
}
