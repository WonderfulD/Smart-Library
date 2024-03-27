package com.ruoyi.borrow.controller;

import com.ruoyi.book.domain.Books;
import com.ruoyi.borrow.domain.BookBorrowing;
import com.ruoyi.borrow.service.IBookBorrowingService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.ZoneId;
import java.util.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 图书借阅信息Controller
 * 
 * @author ruoyi
 * @date 2024-03-12
 */
@RestController
@RequestMapping("/borrow/BookBorrowing")
public class BookBorrowingController extends BaseController
{
    @Autowired
    private IBookBorrowingService bookBorrowingService;

    /**
     * 查询图书借阅信息列表
     */
    @PreAuthorize("@ss.hasPermi('borrow:BookBorrowing:list')")
    @GetMapping("/list")
    public TableDataInfo list(BookBorrowing bookBorrowing)
    {
        startPage();
        List<BookBorrowing> list = bookBorrowingService.selectBookBorrowingList(bookBorrowing);
        return getDataTable(list);
    }


    /**
     * 查询图书借阅信息列表，并添加借阅状态
     */
    @PreAuthorize("@ss.hasPermi('borrow:BookBorrowing:list')")
    @GetMapping("/listWithStatus")
    public TableDataInfo listWithStatus(BookBorrowing bookBorrowing) {
        startPage();
        bookBorrowing.setLibraryId(SecurityUtils.getDeptId());
        List<BookBorrowing> list = bookBorrowingService.selectBookBorrowingListByDept(bookBorrowing);
        for (BookBorrowing borrowing : list) {
            borrowing.setStatus(getBorrowingStatus(borrowing));
        }
        return getDataTable(list);
    }

    /**
     * 获取借阅状态
     */
    private int getBorrowingStatus(BookBorrowing borrowing) {
        if (borrowing.getReturnDate() != null) {
            if (borrowing.getReturnDate().compareTo(borrowing.getDueDate()) <= 0) {
                return 0; //如期归还
            } else {
                return 2; //逾期归还
            }
        } else {
            Date today = new Date();
            if (today.compareTo(borrowing.getDueDate()) <= 0) {
                return 1; //借阅正常
            } else {
                return 3; //逾期未还
            }
        }
    }


    /**
     * 根据当前登录管理员所在图书馆ID，查询图书借阅信息列表
     */
    @PreAuthorize("@ss.hasPermi('borrow:BookBorrowing:list')")
    @GetMapping("/listByDept")
    public TableDataInfo listByDept(BookBorrowing bookBorrowing) {
        startPage();
        bookBorrowing.setLibraryId(SecurityUtils.getDeptId()); // 设置当前用户所在部门ID
        List<BookBorrowing> list = bookBorrowingService.selectBookBorrowingListByDept(bookBorrowing);
        return getDataTable(list);
    }

    /** 根据当前登录管理员所在图书馆ID，查询会员数
     *
     */
    @GetMapping("/listRecentMembers")
    public AjaxResult listRecentMembers() {
        LocalDate today = LocalDate.now();
        LocalDate sevenDaysAgo = today.minusDays(7);

        // 获取当前图书馆的所有借阅信息
        BookBorrowing bookBorrowing = new BookBorrowing();
        bookBorrowing.setLibraryId(SecurityUtils.getDeptId());
        List<BookBorrowing> bookBorrowingList = bookBorrowingService.selectBookBorrowingListByDept(bookBorrowing);

        // 使用Set去重，存储不同的readerId
        Map<LocalDate, Set<Long>> dailyMembers = new TreeMap<>();

        // 初始化最近七天的会员集合
        for (int i = 1; i < 8; i++) {
            dailyMembers.put(sevenDaysAgo.plusDays(i), new HashSet<>());
        }

        // 遍历借阅信息，填充每天的会员集合
        for (BookBorrowing borrowing : bookBorrowingList) {
            LocalDate borrowDate = borrowing.getBorrowDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (!borrowDate.isBefore(sevenDaysAgo) && !borrowDate.isAfter(today)) {
                dailyMembers.get(borrowDate).add(borrowing.getReaderId());
            }
        }

        // 计算累计会员数量
        List<Integer> recentMembersCounts = new ArrayList<>();
        Set<Long> cumulativeMembers = new HashSet<>();
        for (LocalDate date : dailyMembers.keySet()) {
            cumulativeMembers.addAll(dailyMembers.get(date));
            recentMembersCounts.add(cumulativeMembers.size());
        }

        LocalDate fourteenDaysAgo = today.minusDays(14);

        // 使用Set去重，存储不同的readerId
        Map<LocalDate, Set<Long>> lastFourteenToSevenDaysMembers = new TreeMap<>();

        // 初始化最近七天的会员集合
        for (int i = 1; i < 8; i++) {
            lastFourteenToSevenDaysMembers.put(fourteenDaysAgo.plusDays(i), new HashSet<>());
        }

        // 遍历借阅信息，填充每天的会员集合
        for (BookBorrowing borrowing : bookBorrowingList) {
            LocalDate borrowDate = borrowing.getBorrowDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (!borrowDate.isBefore(fourteenDaysAgo) && !borrowDate.isAfter(sevenDaysAgo)) {
                lastFourteenToSevenDaysMembers.get(borrowDate).add(borrowing.getReaderId());
            }
        }

        // 计算累计会员数量
        List<Integer> lastMembersCounts = new ArrayList<>();
        Set<Long> lastFourteenToSevenDaysCumulativeMembers = new HashSet<>();
        for (LocalDate date : lastFourteenToSevenDaysMembers.keySet()) {
            lastFourteenToSevenDaysCumulativeMembers.addAll(lastFourteenToSevenDaysMembers.get(date));
            lastMembersCounts.add(lastFourteenToSevenDaysCumulativeMembers.size());
        }

        // 预计会员数量列表
        List<Integer> estimatedMembersCount = estimateFutureMemberCounts(lastMembersCounts);

        System.out.println("lastFourteenToSevenDaysMembers" + lastFourteenToSevenDaysMembers);

        System.out.println(lastMembersCounts);
        System.out.println(estimatedMembersCount);

        // 封装结果返回
        Map<String, Object> result = new HashMap<>();
        result.put("recentMembersCounts", recentMembersCounts);
        result.put("estimatedMembersCount", estimatedMembersCount);

        return AjaxResult.success(result);
    }

    /**
     * 预估未来七天的会员量
     */
    public List<Integer> estimateFutureMemberCounts(List<Integer> recentMembersCounts) {
        if (recentMembersCounts == null || recentMembersCounts.size() <= 1) {
            throw new IllegalArgumentException("The list of recent member counts must contain at least two days of data.");
        }

        // 基于平均增长率进行预估
        int lastKnownCount = recentMembersCounts.get(recentMembersCounts.size() - 1);

        // 初始化预估列表，开始时仅包含最后一个已知值
        List<Integer> estimatedMemberCounts = new ArrayList<>();
        estimatedMemberCounts.add(lastKnownCount);

        // 进行七天的预估
        for (int i = 1; i <= 7; i++) {

            int nextDayCount = lastKnownCount;

            // 检查是否需要调整（即当前是连续的第三个数字）
            if (i >= 3 && estimatedMemberCounts.get(i - 2).equals(estimatedMemberCounts.get(i - 3))
                    && estimatedMemberCounts.get(i - 2).equals(estimatedMemberCounts.get(i - 1))) {
                nextDayCount = (int) Math.round(nextDayCount * 1.10); // 上浮10%
                nextDayCount = Math.max(nextDayCount, lastKnownCount + 1); // 确保至少增加1
            }

            estimatedMemberCounts.add(nextDayCount);
            lastKnownCount = nextDayCount;
        }

        // 移除初始化时加入的最后一个已知值
        estimatedMemberCounts.remove(0);

        return estimatedMemberCounts;
    }


    /**
     * 导出图书借阅信息列表
     */
    @PreAuthorize("@ss.hasPermi('borrow:BookBorrowing:export')")
    @Log(title = "图书借阅信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BookBorrowing bookBorrowing)
    {
        List<BookBorrowing> list = bookBorrowingService.selectBookBorrowingList(bookBorrowing);
        ExcelUtil<BookBorrowing> util = new ExcelUtil<BookBorrowing>(BookBorrowing.class);
        util.exportExcel(response, list, "图书借阅信息数据");
    }

    /**
     * 获取图书借阅信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('borrow:BookBorrowing:query')")
    @GetMapping(value = "/{borrowId}")
    public AjaxResult getInfo(@PathVariable("borrowId") Long borrowId)
    {
        return success(bookBorrowingService.selectBookBorrowingByBorrowId(borrowId));
    }

    /**
     * 新增图书借阅信息
     */
    @PreAuthorize("@ss.hasPermi('borrow:BookBorrowing:add')")
    @Log(title = "图书借阅信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BookBorrowing bookBorrowing)
    {

        return toAjax(bookBorrowingService.insertBookBorrowing(bookBorrowing));
    }

    /**
     * 修改图书借阅信息
     */
    @PreAuthorize("@ss.hasPermi('borrow:BookBorrowing:edit')")
    @Log(title = "图书借阅信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BookBorrowing bookBorrowing)
    {
        return toAjax(bookBorrowingService.updateBookBorrowing(bookBorrowing));
    }

    /**
     * 删除图书借阅信息
     */
    @PreAuthorize("@ss.hasPermi('borrow:BookBorrowing:remove')")
    @Log(title = "图书借阅信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{borrowIds}")
    public AjaxResult remove(@PathVariable Long[] borrowIds)
    {
        return toAjax(bookBorrowingService.deleteBookBorrowingByBorrowIds(borrowIds));
    }
}
