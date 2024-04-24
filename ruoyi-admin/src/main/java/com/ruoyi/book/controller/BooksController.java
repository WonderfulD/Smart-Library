package com.ruoyi.book.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.borrow.controller.BookBorrowingController;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.rate.domain.BookRatings;
import com.ruoyi.rate.service.IBookRatingsService;
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

import static com.ruoyi.borrow.controller.BookBorrowingController.getBorrowingStatus;
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

    @Autowired
    private IBookRatingsService bookRatingsService;

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
     * 根据读者ID查询读者借阅图书类别分布
     */
    @GetMapping("/borrowedBooksCategoryDistribution")
    public AjaxResult getCategoryDistributionByReaderId() {
        try {
            // 获取当前读者的所有借阅信息
            BookBorrowing bookBorrowing = new BookBorrowing();
            bookBorrowing.setReaderId(SecurityUtils.getUserId());
            List<BookBorrowing> bookBorrowingList = bookBorrowingService.selectBookBorrowingListByReaderId(bookBorrowing);

            // 统计每个类别的图书数量
            Map<String, Integer> categoryCount = new HashMap<>();
            for (BookBorrowing borrowing : bookBorrowingList) {
                // 获取每本图书的详细信息
                Books bookInfo = booksService.selectBooksByBookId(borrowing.getBookId());
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
            // 计算截至到specificDay的借阅量
            long count = bookBorrowingList.stream()
                    .filter(borrowing -> {
                        LocalDate borrowDate = borrowing.getBorrowDate().toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();
                        return !borrowDate.isAfter(specificDay);
                    })
                    .count();
            recentBorrowsCounts.set(i - 1, (int) count);
        }

        // 初始化最近14天至最近7天的借阅列表
        List<Integer> lastBorrowsCounts = new ArrayList<>(Collections.nCopies(7, 0));

        LocalDate fourteenDaysAgo = today.minusDays(14);

        // 遍历最近14天至最近7天
        for (int i = 1; i < 8; i++) {
            LocalDate specificDay = fourteenDaysAgo.plusDays(i);
            // 计算截至到specificDay的借阅量
            long count = bookBorrowingList.stream()
                    .filter(borrowing -> {
                        LocalDate borrowDate = borrowing.getBorrowDate().toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();
                        return !borrowDate.isAfter(specificDay);
                    })
                    .count();
            lastBorrowsCounts.set(i - 1, (int) count);
        }

        // 预计借阅量列表
        List<Integer> estimatedBorrowsCount = predictNextWeek(lastBorrowsCounts);

        System.out.println(recentBorrowsCounts);
        System.out.println(lastBorrowsCounts);
        System.out.println(estimatedBorrowsCount);

        // 封装结果返回
        Map<String, Object> result = new HashMap<>();
        result.put("recentBorrowsCounts", recentBorrowsCounts);
        result.put("estimatedBorrowsCount", estimatedBorrowsCount);

        return AjaxResult.success(result);
    }

    /**
     * 根据图书馆ID查询图书借阅量列表
     */
    @GetMapping("/listBorrowsListByLibraryId")
    public AjaxResult listBorrowsListByLibraryId() {
        BookBorrowing bookBorrowing = new BookBorrowing();
        bookBorrowing.setLibraryId(SecurityUtils.getDeptId());
        List<BookBorrowing> bookBorrowingList = bookBorrowingService.selectBookBorrowingListByDept(bookBorrowing);

        // 使用Map存储借阅次数
        Map<Long, Integer> borrowCounts = new HashMap<>();
        for (BookBorrowing borrowing : bookBorrowingList) {
            borrowCounts.put(borrowing.getBookId(), borrowCounts.getOrDefault(borrowing.getBookId(), 0) + 1);
        }

        // 使用List存储最终结果，每个元素包含图书详细信息和借阅次数
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : borrowCounts.entrySet()) {
            Long bookId = entry.getKey();
            Books bookInfo = booksService.selectBooksByBookId(bookId);

            // 创建一个包含图书详细信息和借阅次数的Map
            Map<String, Object> bookDetails = new HashMap<>();
            bookDetails.put("bookId", bookId);
            bookDetails.put("title", bookInfo.getTitle());
            bookDetails.put("coverUrl", bookInfo.getCoverUrl());
            bookDetails.put("borrowCount", entry.getValue());

            resultList.add(bookDetails);
        }

        // 对resultList按照borrowCount倒序排序
        resultList.sort((map1, map2) -> Integer.compare((int)map2.get("borrowCount"), (int)map1.get("borrowCount")));

        return AjaxResult.success(resultList);
    }

    /**
     * 根据读者id获取推荐图书列表
     */
    @GetMapping("/recommendations")
    public AjaxResult getBookRecommendations() {
        try {
            Long readerId = SecurityUtils.getUserId();
            BookBorrowing bookBorrowing = new BookBorrowing();
            bookBorrowing.setReaderId(readerId);
            // 获取此人已经借阅的书籍ID列表
            List<Long> borrowedBookIds = bookBorrowingService.selectBookBorrowingListByReaderId(bookBorrowing)
                    .stream()
                    .map(BookBorrowing::getBookId)
                    .collect(Collectors.toList());

            // 获取所有书籍列表
            List<Books> allBooks = booksService.selectBooksList(new Books());

            // 过滤出此人没有借阅过的书籍
            List<Books> unborrowedBooks = allBooks.stream()
                    .filter(book -> !borrowedBookIds.contains(book.getBookId()))
                    .collect(Collectors.toList());

            // 获取所有书籍的评分信息
            List<BookRatings> bookRatingsList = bookRatingsService.selectBookRatingsList(new BookRatings());

            // 创建一个Map来存储书籍ID和对应的评分
            Map<Long, Double> bookRatingsMap = new HashMap<>();
            for (BookRatings rating : bookRatingsList) {
                bookRatingsMap.put(rating.getBookId(), Double.valueOf(rating.getRating()));
            }

            // 对书籍列表按照评分进行排序
            unborrowedBooks.sort((book1, book2) -> {
                Double rating1 = bookRatingsMap.getOrDefault(book1.getBookId(), 0.0);
                Double rating2 = bookRatingsMap.getOrDefault(book2.getBookId(), 0.0);
                return Double.compare(rating2, rating1);
            });

            return AjaxResult.success("获取成功", unborrowedBooks);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("获取未借阅书籍并排序失败");
        }
    }




    /**
     * 查询所有图书馆图书借阅量列表
     */
    @GetMapping("/listBorrowsList")
    public AjaxResult listBorrowsList() {
        BookBorrowing bookBorrowing = new BookBorrowing();
        List<BookBorrowing> bookBorrowingList = bookBorrowingService.selectBookBorrowingListByDept(bookBorrowing);

        // 使用Map存储借阅次数
        Map<Long, Integer> borrowCounts = new HashMap<>();
        for (BookBorrowing borrowing : bookBorrowingList) {
            borrowCounts.put(borrowing.getBookId(), borrowCounts.getOrDefault(borrowing.getBookId(), 0) + 1);
        }

        // 使用List存储最终结果，每个元素包含图书详细信息和借阅次数
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : borrowCounts.entrySet()) {
            Long bookId = entry.getKey();
            Books bookInfo = booksService.selectBooksByBookId(bookId);

            // 创建一个包含图书详细信息和借阅次数的Map
            Map<String, Object> bookDetails = new HashMap<>();
            bookDetails.put("bookId", bookId);
            bookDetails.put("title", bookInfo.getTitle());
            bookDetails.put("coverUrl", bookInfo.getCoverUrl());
            bookDetails.put("borrowCount", entry.getValue());

            resultList.add(bookDetails);
        }

        // 对resultList按照borrowCount倒序排序
        resultList.sort((map1, map2) -> Integer.compare((int)map2.get("borrowCount"), (int)map1.get("borrowCount")));

        return AjaxResult.success(resultList);
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
        List<BookBorrowing> list = bookBorrowingService.selectBookBorrowingListByReaderId(bookBorrowing);
        return getDataTable(list);
    }


    /**
     * 根据借阅人ID查询应还图书列表，并添加借阅状态
     */
//    @PreAuthorize("@ss.hasPermi('book:BookInfo:list')")
    @GetMapping("/returnListWithStatusByReader")
    public TableDataInfo returnListWithStatusByReader(BookBorrowing bookBorrowing)
    {
        BookBorrowingController.startPageByBorrowDateDesc();
        bookBorrowing.setReaderId(SecurityUtils.getUserId()); // 设置当前用户ID
        System.out.println(bookBorrowing.getReaderId());
        bookBorrowing.setPendingStatus(1L);
        List<BookBorrowing> list = bookBorrowingService.selectBookBorrowingByPendingStatusWithNullReturnDate(bookBorrowing);
        for (BookBorrowing borrowing : list) {
            borrowing.setStatus((long) getBorrowingStatus(borrowing));

        }
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
        books.setLibraryId(SecurityUtils.getDeptId());
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
            if (book.getStatus() == 0 || book.getStatus() == 2 ) {
                return AjaxResult.error("图书已被借出");
            }
            Long readerId = request.getReaderId();
            Long libraryId = request.getLibraryId();
            Date borrowDate = request.getBorrowDate();
            // 创建并保存借阅记录
            BookBorrowing bookBorrowing = new BookBorrowing();
            bookBorrowing.setBookId(bookId);
            bookBorrowing.setReaderId(readerId);
            bookBorrowing.setLibraryId(libraryId);
            bookBorrowing.setBorrowDate(borrowDate);
            Long borrowMethod = request.getBorrowMethod();
            bookBorrowing.setBorrowMethod(borrowMethod);
            //设置borrowId为日期+随机数
            long timestamp = new Date().getTime();
            long randomNumber = ThreadLocalRandom.current().nextLong(1, 1000);
            long borrowId = (timestamp % 100000000L) * 1000 + randomNumber; // 确保borrowId适合Long存储
            bookBorrowing.setBorrowId(borrowId); // 设置生成的借阅编号
            //设置借阅状态待审核
            bookBorrowing.setPendingStatus(2L);
            //设置借阅备注
            String comments = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 创建日期格式化对象
            String formattedDate = sdf.format(borrowDate); // 格式化日期
            if (borrowMethod == 0L) {  //到馆
                comments = "到馆取阅, 取阅时间为: " + formattedDate;
            } else if (borrowMethod == 1L) {
                comments = "邮寄取阅, 邮寄地址为: " + request.getComments();

            }
            bookBorrowing.setComments(comments);
            bookBorrowingService.insertBookBorrowing(bookBorrowing);
            // 更新图书状态为待审核
            book.setStatus(2L);
            booksService.updateBooks(book);

            return AjaxResult.success("借阅成功，待审核");
        } catch (Exception e) {
            // 如果有任何异常，Spring会回滚事务
            e.printStackTrace();
            return AjaxResult.error("借阅失败，请稍后再试");
        }
    }


    /**
     * 处理管理员确认图书归还
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
            if (book.getStatus() == 1) {
                return AjaxResult.error("图书已被归还");
            }else if (book.getStatus() == 2) {
                return AjaxResult.error("当前无法确认此图书");
            }
            // 更新图书状态为已归还
            book.setStatus(1L);
            booksService.updateBooks(book);
            Long borrowId = request.getBorrowId();
            Date dueDate = request.getDueDate();
            Date returnDate = new Date();
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
                    fine = 30L; //超过100天,上限30元
                }
            }
            // 创建并修改借阅记录
            BookBorrowing bookBorrowing = new BookBorrowing();
            bookBorrowing.setBookId(bookId);
            bookBorrowing.setBorrowId(borrowId);
            bookBorrowing.setReturnDate(returnDate);
            bookBorrowing.setComments(comment);
            bookBorrowing.setFine(BigDecimal.valueOf(fine));
            bookBorrowingService.updateBookBorrowing(bookBorrowing);

            return AjaxResult.success("图书已确认归还");
        } catch (Exception e) {
            // 如果有任何异常，Spring会回滚事务
            e.printStackTrace();
            return AjaxResult.error("确认归还失败，请稍后再试");
        }
    }

    /**
     * 处理读者图书归还
     * @return AjaxResult 响应结果
     */
    @Transactional
    @PostMapping("/readerReturn")
    public AjaxResult handleReaderReturn(@RequestBody BookBorrowing request) {
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
            } else if (book.getStatus() == 2) {
                return AjaxResult.error("当前无法归还此图书");
            }
            // 创建并修改借阅记录
            BookBorrowing bookBorrowing = new BookBorrowing();
            Long borrowId = request.getBorrowId();
            Date returnDate = request.getReturnDate();
            Long returnMethod = request.getReturnMethod();
            String comment;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 创建日期格式化对象
            String formattedDate = sdf.format(returnDate); // 格式化日期

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
            bookBorrowingService.updateBookBorrowing(bookBorrowing);
            return AjaxResult.success("已提交，您的预计归还日期为:" + formattedDate);
        } catch (Exception e) {
            // 如果有任何异常，Spring会回滚事务
            e.printStackTrace();
            return AjaxResult.error("归还失败，请稍后再试");
        }
    }



    /**
     * 处理借阅延期
     * @return AjaxResult 响应结果
     */
    @Transactional
    @PostMapping("/extension")
    public AjaxResult handleExtension(@RequestBody BookBorrowing request) {
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
            //检查是否已有延期
            BookBorrowing borrowing = bookBorrowingService.selectBookBorrowingByBorrowId(borrowId);
            if (borrowing.getComments().equals("延期15天成功")) {
                return AjaxResult.error("不允许延期，你已经申请过延期！");
            }
            Date dueDate = request.getDueDate();
            Calendar calendar = Calendar.getInstance(); // 获取一个Calendar实例
            calendar.setTime(dueDate); // 设置calendar的时间为dueDate
            calendar.add(Calendar.DAY_OF_MONTH, 15); // 在dueDate上增加15天

            // 获取新的日期，即extensionDate
            Date extensionDate = calendar.getTime();
            // 创建并修改借阅记录
            BookBorrowing bookBorrowing = new BookBorrowing();
            bookBorrowing.setBookId(bookId);
            bookBorrowing.setBorrowId(borrowId);
            bookBorrowing.setDueDate(extensionDate);
            bookBorrowing.setComments("延期15天成功");
            bookBorrowingService.updateBookBorrowing(bookBorrowing);
            return AjaxResult.success("延期成功");
        } catch (Exception e) {
            // 如果有任何异常，Spring会回滚事务
            e.printStackTrace();
            return AjaxResult.error("延期失败，请稍后再试");
        }
    }
}
