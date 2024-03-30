package com.ruoyi.borrow.controller;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static com.ruoyi.prediction.Prediction.predictNextWeek;
import static com.ruoyi.sort.BookBorrowingSorter.sortBookBorrowingsByBorrowDateDesc;


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
     * 根据图书馆Id查询图书借阅信息列表，并添加借阅状态
     */
    @PreAuthorize("@ss.hasPermi('borrow:BookBorrowing:list')")
    @GetMapping("/listWithStatus")
    public TableDataInfo listWithStatus(BookBorrowing bookBorrowing) {
        bookBorrowing.setLibraryId(SecurityUtils.getDeptId());
        List<BookBorrowing> list = bookBorrowingService.selectBookBorrowingListByDept(bookBorrowing);
        for (BookBorrowing borrowing : list) {
            borrowing.setStatus((long) getBorrowingStatus(borrowing));
        }
        return getDataTable(sortBookBorrowingsByBorrowDateDesc(list));
    }

    /**
     * 根据读者Id查询图书借阅信息列表，并添加借阅状态
     */
    @PreAuthorize("@ss.hasPermi('borrowbrowsing:BorrowBrowsing')")
    @GetMapping("/listWithStatusByReaderId")
    public TableDataInfo listWithStatusByReaderId(BookBorrowing bookBorrowing) {
        bookBorrowing.setReaderId(SecurityUtils.getUserId());
        List<BookBorrowing> list = bookBorrowingService.selectBookBorrowingListByReaderId(bookBorrowing);
        for (BookBorrowing borrowing : list) {
            borrowing.setStatus((long) getBorrowingStatus(borrowing));
        }
        return getDataTable(sortBookBorrowingsByBorrowDateDesc(list));
    }

    /**
     * 获取借阅状态
     */
    public static int getBorrowingStatus(BookBorrowing borrowing) {
        if (borrowing.getPendingStatus() == 0L) {
          return 5;       //
        } else if (borrowing.getDueDate() == null) {
            return 4;     //待审核
        } else if (borrowing.getReturnDate() != null) {
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


    /**
     * 根据当前登录管理员所在图书馆ID，查询图书借阅待审批列表
     */
//    @PreAuthorize("@ss.hasPermi('borrow:BookBorrowing:list')")
    @GetMapping("/listPendingByDept")
    public TableDataInfo listPendingByDept(BookBorrowing bookBorrowing) {
        startPage();
        bookBorrowing.setLibraryId(SecurityUtils.getDeptId()); // 设置当前用户所在部门ID
        bookBorrowing.setPendingStatus(2L);
        List<BookBorrowing> list = bookBorrowingService.selectBookBorrowingListByDept(bookBorrowing);
        return getDataTable(list);
    }



    /** 根据当前登录管理员所在图书馆ID，查询当前会员数
     *
     */
    @GetMapping("/getTotalMembers")
    public AjaxResult getTotalMembers() throws Exception {
        LocalDate today = LocalDate.now();
        Integer membersCounts = bookBorrowingService.countDistinctReaderIdsByDate(Date.from(today.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
        Map<String, Object> result = new HashMap<>();
        result.put("MembersCounts", membersCounts);
        return AjaxResult.success(result);
    }


    /** 根据当前登录管理员所在图书馆ID，查询会员数列表
     *
     */
    @GetMapping("/listRecentMembers")
    public AjaxResult listRecentMembers() throws Exception {
        LocalDate fourteenDaysAgo = LocalDate.now().minusDays(14);

        Map<String, Object> result = new HashMap<>();

        List<Integer> totalMembersCounts = new ArrayList<>();

        for (int i = 1; i < 15; i++) {
            totalMembersCounts.add(bookBorrowingService.countDistinctReaderIdsByDate(Date.from(fourteenDaysAgo.plusDays(i).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())));
        }

        System.out.println("totalMembersCounts:" + totalMembersCounts);

        result.put("recentMembersCounts", totalMembersCounts.subList(7, 14));
        result.put("estimatedMembersCount", predictNextWeek(totalMembersCounts.subList(0, 8)));

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
//    @PreAuthorize("@ss.hasPermi('borrow:BookBorrowing:query')")
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
