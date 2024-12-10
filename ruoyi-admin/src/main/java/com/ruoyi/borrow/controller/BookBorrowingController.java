package com.ruoyi.borrow.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.borrow.domain.BookBorrowing;
import com.ruoyi.borrow.service.IBookBorrowingService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.sql.SqlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    public static void startPageByBorrowDateDesc() {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();

        String orderBy =  SqlUtil.escapeOrderBySql(SqlUtil.escapeOrderBySql("borrow_date desc"));
        Boolean reasonable = pageDomain.getReasonable();
        PageHelper.startPage(pageNum, pageSize, orderBy).setReasonable(reasonable);
    }

    /**
     * 根据读者Id查询图书借阅信息列表，并添加借阅状态
     */
    @PreAuthorize("@ss.hasPermi('borrowbrowsing:BorrowBrowsing')")
    @GetMapping("/listWithStatusByReaderId")
    public TableDataInfo listWithStatusByReaderId(BookBorrowing bookBorrowing) {
        startPageByBorrowDateDesc();
        bookBorrowing.setReaderId(SecurityUtils.getUserId());
        List<BookBorrowing> list = bookBorrowingService.selectBookBorrowingListByReaderId(bookBorrowing);
        for (BookBorrowing borrowing : list) {
            borrowing.setStatus((long) getBorrowingStatus(borrowing));
        }
        return getDataTable(list);
    }

    /**
     * 获取借阅状态
     */
    public static int getBorrowingStatus(BookBorrowing borrowing) {
        if(borrowing.getReturnMethod() != null && borrowing.getReturnDate() == null) {
            return 6;
        }
        if (borrowing.getPendingStatus() == 0L) {
          return 5;       //借阅拒绝
        } else if (borrowing.getDueDate() == null) {
            return 4;     //待审核
        } else if (borrowing.getReturnDate() != null) {
            if (borrowing.getReturnDate().compareTo(borrowing.getDueDate()) <= 0) {
                return 0; //如期归还
            } else {
                return 2; //逾期归还
            }
        } else {
            LocalDate today = LocalDate.now();
            if (today.isBefore(borrowing.getDueDate())) {
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
     * 根据当前登录管理员所在图书馆ID，查询每天借阅图书所属种类列表
     */
    @GetMapping("/listCategoryCountsByDay")
    public AjaxResult listCategoryCountsByDay() {
        LocalDate today = LocalDate.now();
        Map<String, Map<String, Integer>> categoryCountsByDay = new LinkedHashMap<>(); // 使用LinkedHashMap保持顺序

        // 遍历近七天
        for (int i = 6; i >= 0; i--) {
            LocalDate targetDate = today.minusDays(i);
            BookBorrowing bookBorrowing = new BookBorrowing();
            bookBorrowing.setLibraryId(SecurityUtils.getDeptId());
            bookBorrowing.setBorrowDate(targetDate); // 设置查询日期

            List<BookBorrowing> dailyList = bookBorrowingService.selectBookBorrowingListByLibraryIdWithCategory(bookBorrowing);
            Map<String, Integer> dailyCategoryCounts = dailyList.stream()
                    .collect(Collectors.groupingBy(BookBorrowing::getCategory, Collectors.summingInt(e -> 1)));

            categoryCountsByDay.put(targetDate.toString(), dailyCategoryCounts);
        }

        return AjaxResult.success(categoryCountsByDay);
    }




    /**
     * 根据当前登录借阅人ID，查询图书借阅信息列表
     */
//    @PreAuthorize("@ss.hasPermi('borrow:BookBorrowing:list')")
    @GetMapping("/listByUser")
    public TableDataInfo listByUser(BookBorrowing bookBorrowing) {
        bookBorrowing.setReaderId(SecurityUtils.getUserId()); // 设置当前用户ID
        List<BookBorrowing> list = bookBorrowingService.selectBookBorrowingListByReaderId(bookBorrowing);
        return getDataTable(list);
    }


    /**
     * 根据当前登录借阅人ID，查询借阅过的图书列表
     */
    @GetMapping("/listByUserDistinctBooks")
    public TableDataInfo listByUserDistinctBooks(BookBorrowing bookBorrowing) {
        // 手动获取分页参数
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();

        bookBorrowing.setReaderId(SecurityUtils.getUserId()); // 设置当前用户ID
        List<BookBorrowing> list = bookBorrowingService.selectBookBorrowingListByReaderId(bookBorrowing);

        // 去重操作，基于图书ID
        Map<Long, BookBorrowing> distinctMap = list.stream()
                .collect(Collectors.toMap(BookBorrowing::getBookId, Function.identity(), (existing, replacement) -> existing));

        // 去重后的列表
        List<BookBorrowing> distinctList = new ArrayList<>(distinctMap.values());

        // 分页处理
        int total = distinctList.size();
        int fromIndex = Math.min((pageNum - 1) * pageSize, total);
        int toIndex = Math.min(pageNum * pageSize, total);

        List<BookBorrowing> pageList = distinctList.subList(fromIndex, toIndex);

        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setRows(pageList);
        rspData.setTotal(total);

        return rspData;
    }


    /**
     * 根据当前登录管理员所在图书馆ID，查询图书借阅待审批列表
     */
//    @PreAuthorize("@ss.hasPermi('borrow:BookBorrowing:list')")
    @GetMapping("/listPendingByDept")
    public TableDataInfo listPendingByDept(BookBorrowing bookBorrowing) {
        startPageByBorrowDateDesc();
        bookBorrowing.setLibraryId(SecurityUtils.getDeptId()); // 设置当前用户所在部门ID
        bookBorrowing.setPendingStatus(2L);
        List<BookBorrowing> list = bookBorrowingService.selectBookBorrowingListByDept(bookBorrowing);
        return getDataTable(list);
    }

    /**
     * 根据当前登录管理员所在图书馆ID，查询图书归还待确认列表
     */
//    @PreAuthorize("@ss.hasPermi('borrow:BookBorrowing:list')")
    @GetMapping("/listReturnPendingByDept")
    public TableDataInfo listReturnPendingByDept(BookBorrowing bookBorrowing) {
        startPageByBorrowDateDesc();
        bookBorrowing.setLibraryId(SecurityUtils.getDeptId()); // 设置当前用户所在部门ID
        bookBorrowing.setReturnMethod(0L);
        List<BookBorrowing> list = new CopyOnWriteArrayList<>();
        List<BookBorrowing> list1 = bookBorrowingService.selectBookBorrowingListByDept(bookBorrowing);
        bookBorrowing.setReturnMethod(1L);
        List<BookBorrowing> list2 = bookBorrowingService.selectBookBorrowingListByDept(bookBorrowing);
        list.addAll(list1);
        list.addAll(list2);
        for (BookBorrowing borrowing : list) {
            if (borrowing.getReturnDate() != null) {
                list.remove(borrowing);
                continue;
            }
            borrowing.setStatus((long) getBorrowingStatus(borrowing));
        }
        return getDataTable(list);
    }



    /**
     * 根据当前登录管理员所在图书馆ID，查询当前会员数
     */
    @GetMapping("/getTotalMembers")
    public AjaxResult getTotalMembers() throws Exception {
        LocalDate today = LocalDate.now();
        Integer membersCounts = bookBorrowingService.countDistinctReaderIdsByDate(Date.from(today.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), SecurityUtils.getDeptId());
        Map<String, Object> result = new HashMap<>();
        result.put("MembersCounts", membersCounts);
        return AjaxResult.success(result);
    }


    /**
     * 根据当前登录管理员所在图书馆ID，查询会员数列表
     */
    @GetMapping("/listRecentMembers")
    public AjaxResult listRecentMembers() throws Exception {
        LocalDate fourteenDaysAgo = LocalDate.now().minusDays(14);

        Map<String, Object> result = new HashMap<>();

        List<Integer> totalMembersCounts = new ArrayList<>();

        for (int i = 1; i < 15; i++) {
            totalMembersCounts.add(bookBorrowingService.countDistinctReaderIdsByDate(Date.from(fourteenDaysAgo.plusDays(i).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), SecurityUtils.getDeptId()));
        }

        result.put("recentMembersCounts", totalMembersCounts.subList(7, 14));
        result.put("estimatedMembersCount", predictNextWeek(totalMembersCounts.subList(0, 8)));

        return AjaxResult.success(result);
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
