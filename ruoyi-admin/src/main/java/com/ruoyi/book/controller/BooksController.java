package com.ruoyi.book.controller;

import java.time.LocalDate;
import java.time.ZoneId;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
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
import com.ruoyi.borrow.domain.BookBorrowing;
import com.ruoyi.borrow.service.IBookBorrowingService;

import java.util.stream.Collectors;

import static com.ruoyi.prediction.Prediction.predictNextWeek;

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
     * 根据图书馆ID查询读者借阅图书类别分布
     */
    @GetMapping("/categoryDistribution")
    public AjaxResult getCategoryDistribution() {
        try {
            // 获取当前图书馆的所有图书信息
            Books books = new Books();
            books.setLibraryId(SecurityUtils.getDeptId());
            List<Books> bookList = booksService.selectBooksListByLibrary(books);

            // 统计每个类别的图书数量
            Map<String, Integer> categoryCount = new HashMap<>();
            for (Books book : bookList) {
                // 获取每本图书的详细信息
                Books bookInfo = booksService.selectBooksByBookId(book.getBookId());
                String category = bookInfo.getCategory();

                // 更新统计信息
                categoryCount.put(category, categoryCount.getOrDefault(category, 0) + 1);
            }
            // 返回统计结果
            return AjaxResult.success("获取成功", categoryCount);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("获取图书类别统计信息失败");
        }
    }

    /**
     * 根据图书馆ID查询最近藏书量
     */
    @GetMapping("/listRecentBooks")
    public AjaxResult listRecentBooks() {
        LocalDate today = LocalDate.now();
        LocalDate sevenDaysAgo = today.minusDays(7);

        // 获取当前图书馆的所有图书信息
        Books books = new Books();
        books.setLibraryId(SecurityUtils.getDeptId());
        List<Books> bookList = booksService.selectBooksListByLibrary(books);

        // 初始化最近七天的藏书量列表
        List<Integer> recentBooksCounts = new ArrayList<>(Collections.nCopies(7, 0));

        // 遍历最近七天
        for (int i = 1; i < 8; i++) {
            LocalDate specificDay = sevenDaysAgo.plusDays(i);
            int finalI = i;
            // 计算截至到specificDay的藏书量
            long count = bookList.stream()
                    .filter(book -> {
                        LocalDate purchaseDate = book.getPurchaseDate().toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();
                        return !purchaseDate.isAfter(specificDay);
                    })
                    .count();
            recentBooksCounts.set(finalI - 1, (int) count);
        }

        // 预计藏书量列表，此示例中返回空列表
        List<Integer> estimatedBooksCount = new ArrayList<>();

        // 封装结果返回
        Map<String, Object> result = new HashMap<>();
        result.put("recentBooksCounts", recentBooksCounts);
        result.put("estimatedBooksCount", estimatedBooksCount);

        return AjaxResult.success(result);
    }


    /**
     * 根据图书馆ID查询最近借阅量
     */
    @GetMapping("/listRecentBorrows")
    public AjaxResult listRecentBorrows() throws Exception {
        LocalDate today = LocalDate.now();
        LocalDate sevenDaysAgo = today.minusDays(7);

        // 获取当前图书馆的所有借阅信息
        BookBorrowing bookBorrowing = new BookBorrowing();
        bookBorrowing.setLibraryId(SecurityUtils.getDeptId());
        List<BookBorrowing> bookBorrowingList = bookBorrowingService.selectBookBorrowingListByDept(bookBorrowing);

        // 初始化最近七天的借阅列表
        List<Integer> recentBorrowsCounts = new ArrayList<>(Collections.nCopies(7, 0));

        // 遍历最近七天
        for (int i = 1; i < 8; i++) {
            LocalDate specificDay = sevenDaysAgo.plusDays(i);
            int finalI = i;
            // 计算截至到specificDay的借阅量
            long count = bookBorrowingList.stream()
                    .filter(borrowing -> {
                        LocalDate borrowDate = borrowing.getBorrowDate().toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();
                        return !borrowDate.isAfter(specificDay);
                    })
                    .count();
            recentBorrowsCounts.set(finalI - 1, (int) count);
        }

        // 初始化最近14天至最近7天的借阅列表
        List<Integer> lastBorrowsCounts = new ArrayList<>(Collections.nCopies(7, 0));

        LocalDate fourteenDaysAgo = today.minusDays(14);

        // 遍历最近14天至最近7天
        for (int i = 1; i < 8; i++) {
            LocalDate specificDay = fourteenDaysAgo.plusDays(i);
            int finalI = i;
            // 计算截至到specificDay的借阅量
            long count = bookBorrowingList.stream()
                    .filter(borrowing -> {
                        LocalDate borrowDate = borrowing.getBorrowDate().toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();
                        return !borrowDate.isAfter(specificDay);
                    })
                    .count();
            lastBorrowsCounts.set(finalI - 1, (int) count);
        }

        // 预计借阅量列表
        List<Integer> estimatedBorrowsCount = predictNextWeek(lastBorrowsCounts);

        System.out.println(lastBorrowsCounts);
        System.out.println(estimatedBorrowsCount);

        // 封装结果返回
        Map<String, Object> result = new HashMap<>();
        result.put("recentBorrowsCounts", recentBorrowsCounts);
        result.put("estimatedBorrowsCount", estimatedBorrowsCount);

        return AjaxResult.success(result);
    }

    /**
     * 预估未来七天的借阅量
     */
    public List<Integer> estimateFutureBorrowCounts(List<Integer> recentBorrowsCounts) {
        List<Integer> estimatedBorrowsCount = new ArrayList<>();

        // 用最后一天的借阅量作为基准开始预估
        int lastDayCount = recentBorrowsCounts.get(recentBorrowsCounts.size() - 1);

        // 用于跟踪连续相同数字的计数器
        int sameCount = 1; // 至少有一个（最后一个日子的计数）

        for (int i = 0; i < 7; i++) {
            // 基于上一天预估下一天的借阅量
            int estimate = lastDayCount; // 这里可以加入复杂的逻辑来改进预估

            // 如果前两个预估值与当前相同，增加10%
            if (i >= 2 && estimatedBorrowsCount.get(i-1).equals(estimatedBorrowsCount.get(i-2)) && sameCount >= 2) {
                estimate = (int) Math.round(estimate * 1.1); // 增加10%
                estimate = Math.max(estimate, lastDayCount + 1); // 确保至少增加1
                sameCount = 0; // 重置连续相同数字的计数器
            }

            estimatedBorrowsCount.add(estimate);
            lastDayCount = estimate;

            // 检查连续相同值的情况
            if (i > 0 && estimate == estimatedBorrowsCount.get(i - 1)) {
                sameCount++; // 增加连续相同计数
            } else {
                sameCount = 1; // 重置计数
            }
        }

        return estimatedBorrowsCount;
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
        Date today = new Date();
        books.setPurchaseDate(today);
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

            //设置borrowId为日期+随机数
            long timestamp = new Date().getTime();
            long randomNumber = ThreadLocalRandom.current().nextLong(1, 1000);
            long borrowId = (timestamp % 100000000L) * 1000 + randomNumber; // 确保borrowId适合Long存储
            bookBorrowing.setBorrowId(borrowId); // 设置生成的借阅编号

            //设置借阅状态
            bookBorrowing.setStatus(0);

            //设置借阅备注
            bookBorrowing.setComments("已借出");

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
