package com.ruoyi.borrowrating.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.wordcloud.TextAnalysisUtils;
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
import com.ruoyi.borrowrating.domain.BorrowRatings;
import com.ruoyi.borrowrating.service.IBorrowRatingsService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

import static com.ruoyi.prediction.Prediction.predictNextWeek;

/**
 * 借阅评分Controller
 *
 * @author ruoyi
 * @date 2024-03-30
 */
@RestController
@RequestMapping("/borrowrating/BorrowRating")
public class BorrowRatingsController extends BaseController
{
    @Autowired
    private IBorrowRatingsService borrowRatingsService;

    /**
     * 查询借阅评分列表
     */
//    @PreAuthorize("@ss.hasPermi('borrowrating:BorrowRating:list')")
    @GetMapping("/list")
    public TableDataInfo list(BorrowRatings borrowRatings)
    {
        startPage();
        List<BorrowRatings> list = borrowRatingsService.selectBorrowRatingsList(borrowRatings);
        return getDataTable(list);
    }




    /**
     * 根据图书馆Id查询读者对此图书馆评分
     */
//    @PreAuthorize("@ss.hasPermi('borrowrating:BorrowRating:list')")
    @GetMapping("/getRatings")
    public AjaxResult getRatings(BorrowRatings borrowRatings)
    {
        borrowRatings.setLibraryId(SecurityUtils.getDeptId());
        List<BorrowRatings> list = borrowRatingsService.selectBorrowRatingsList(borrowRatings);
        if (list.isEmpty()) {
            return success(0);
        }else {
            long score = 0;
            for (BorrowRatings rating : list) {
                score += rating.getLibrarySatisfaction() * 20;
            }
            score = score / list.size();
            return success((int) score);
        }
    }


    /**
     * 获取当前图书馆的词云数据
     */
    @GetMapping("/getWordCloudByLibraryId")
    public AjaxResult getWordCloudByLibraryId() {
        // 通过SecurityUtils获取当前用户所属图书馆的ID
        Long libraryId = SecurityUtils.getDeptId();

        // 创建BorrowRatings实例并设置libraryId
        BorrowRatings borrowRatings = new BorrowRatings();
        borrowRatings.setLibraryId(libraryId);

        // 查询与此libraryId相关的所有借阅评分记录
        List<BorrowRatings> ratingsList = borrowRatingsService.selectBorrowRatingsList(borrowRatings);

        // 提取所有建议文本
        List<String> suggestions = ratingsList.stream()
                .map(BorrowRatings::getSuggestions)
                .filter(suggestion -> suggestion != null && !suggestion.isEmpty())
                .collect(Collectors.toList());

        // 使用TextAnalysisUtils生成词云数据
        List<Map<String, Object>> wordCloudData = TextAnalysisUtils.generateWordCloudData(suggestions);

        // 返回封装好的词云数据
        return AjaxResult.success(wordCloudData);
    }

    /**
     * 获取当前图书馆的雷达图数据
     */
    @GetMapping("/getRadarByLibraryId")
    public AjaxResult getRadarByLibraryId() {
        long libraryId = SecurityUtils.getDeptId(); // 从安全上下文获取当前图书馆ID

        // 创建BorrowRatings实例并设置libraryId
        BorrowRatings query = new BorrowRatings();
        query.setLibraryId(libraryId);

        // 查询与此libraryId相关的所有借阅评分记录
        List<BorrowRatings> ratingsList = borrowRatingsService.selectBorrowRatingsList(query);
        if (ratingsList.isEmpty()) {
            return AjaxResult.success("No data available", new int[6]); // 如果没有数据，返回空的雷达图数据
        }

        // 初始化计数器
        int[] counts = new int[5]; // 五个类别的计数器
        long totalRecommendationScore = 0; // 推荐意愿总得分

        // 遍历每条评分记录
        for (BorrowRatings rating : ratingsList) {
            if (rating.getSelectionReasons() != null && !rating.getSelectionReasons().isEmpty()) {
                // 解析选择理由并统计
                String[] reasons = rating.getSelectionReasons().split(",");
                for (String reason : reasons) {
                    int reasonIndex = Integer.parseInt(reason);
                    if (reasonIndex >= 0 && reasonIndex < counts.length) {
                        counts[reasonIndex]++;
                    }
                }
            }

            // 根据推荐意愿计算得分
            int recommendation = Math.toIntExact(rating.getRecommendationWillingness());
            if (recommendation == 1) {
                totalRecommendationScore += 50;
            } else if (recommendation == 2) {
                totalRecommendationScore += 100;
            }
        }

        // 计算每个类别的百分比
        int[] percentages = new int[5];
        for (int i = 0; i < counts.length; i++) {
            percentages[i] = counts[i] * 100 / ratingsList.size();
        }

        // 计算推荐意愿的平均分
        int averageRecommendationScore = (int) totalRecommendationScore / ratingsList.size();

        // 组装最终的雷达图数据
        int[] radarData = new int[6];
        System.arraycopy(percentages, 0, radarData, 0, percentages.length);
        radarData[5] = averageRecommendationScore; // 将推荐意愿平均分作为第六个数据点

        return AjaxResult.success("Radar Chart Data", radarData);
    }


    /**
     * 根据图书馆Id查询读者对此图书馆评分列表
     */
    @GetMapping("/listRecentRatings")
    public AjaxResult listRecentRatings() throws Exception {
        LocalDate today = LocalDate.now();
        LocalDate sevenDaysAgo = today.minusDays(7);
        LocalDate fourteenDaysAgo = today.minusDays(14);

        // 获取当前图书馆的所有评分信息
        BorrowRatings borrowRatings = new BorrowRatings();
        borrowRatings.setLibraryId(SecurityUtils.getDeptId());
        List<BorrowRatings> borrowRatingsList = borrowRatingsService.selectBorrowRatingsList(borrowRatings);

        // 初始化最近七天的平均评分列表
        List<Integer> recentRatingsAverages = new ArrayList<>(Collections.nCopies(7, 0));
        // 初始化最近14天至最近7天的平均评分列表
        List<Integer> lastRatingsAverages = new ArrayList<>(Collections.nCopies(7, 0));

        // 计算最近七天的截至到那天的平均评分
        for (int i = 0; i < 7; i++) {
            LocalDate specificDay = sevenDaysAgo.plusDays(i + 1);
            final LocalDate endDay = specificDay; // 确定筛选截止的日期
            double average = borrowRatingsList.stream()
                    .filter(borrowRatings1 -> {
                        LocalDate ratingDate = borrowRatings1.getRatingDate().toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();
                        return !ratingDate.isAfter(endDay); // 包括这一天及之前的所有评分
                    })
                    .mapToLong(BorrowRatings::getLibrarySatisfaction)
                    .average()
                    .orElse(0.0);
            recentRatingsAverages.set(i, (int) (average * 20)); // 取整可能会导致精度损失
        }

        // 类似地计算最近14天至最近7天的截至到那天的平均评分
        for (int i = 0; i < 7; i++) {
            LocalDate specificDay = fourteenDaysAgo.plusDays(i + 1);
            final LocalDate endDay = specificDay;
            double average = borrowRatingsList.stream()
                    .filter(borrowRatings1 -> {
                        LocalDate ratingDate = borrowRatings1.getRatingDate().toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();
                        return !ratingDate.isAfter(endDay); // 包括这一天及之前的所有评分
                    })
                    .mapToLong(BorrowRatings::getLibrarySatisfaction)
                    .average()
                    .orElse(0.0);
            lastRatingsAverages.set(i, (int)average * 20); // 取整
        }

        // 封装结果返回
        Map<String, Object> result = new HashMap<>();

        result.put("recentRatingsCounts", recentRatingsAverages);
        result.put("estimatedRatingsCounts", predictNextWeek(lastRatingsAverages)); // 确保这里的处理也适用于整数列表

        return AjaxResult.success(result);
    }




    /**
     * 导出借阅评分列表
     */
//    @PreAuthorize("@ss.hasPermi('borrowrating:BorrowRating:export')")
    @Log(title = "借阅评分", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BorrowRatings borrowRatings)
    {
        List<BorrowRatings> list = borrowRatingsService.selectBorrowRatingsList(borrowRatings);
        ExcelUtil<BorrowRatings> util = new ExcelUtil<BorrowRatings>(BorrowRatings.class);
        util.exportExcel(response, list, "借阅评分数据");
    }

    /**
     * 获取借阅评分详细信息
     */
//    @PreAuthorize("@ss.hasPermi('borrowrating:BorrowRating:query')")
    @GetMapping(value = "/{borrowId}")
    public AjaxResult getInfo(@PathVariable("borrowId") Long borrowId)
    {
        return success(borrowRatingsService.selectBorrowRatingsByBorrowId(borrowId));
    }

    /**
     * 新增借阅评分
     */
//    @PreAuthorize("@ss.hasPermi('borrowrating:BorrowRating:add')")
    @Log(title = "借阅评分", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BorrowRatings borrowRatings)
    {
        System.out.println(borrowRatings);
        return toAjax(borrowRatingsService.insertBorrowRatings(borrowRatings));
    }

    /**
     * 修改借阅评分
     */
//    @PreAuthorize("@ss.hasPermi('borrowrating:BorrowRating:edit')")
    @Log(title = "借阅评分", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BorrowRatings borrowRatings)
    {
        return toAjax(borrowRatingsService.updateBorrowRatings(borrowRatings));
    }

    /**
     * 删除借阅评分
     */
//    @PreAuthorize("@ss.hasPermi('borrowrating:BorrowRating:remove')")
    @Log(title = "借阅评分", businessType = BusinessType.DELETE)
	@DeleteMapping("/{borrowIds}")
    public AjaxResult remove(@PathVariable Long[] borrowIds)
    {
        return toAjax(borrowRatingsService.deleteBorrowRatingsByBorrowIds(borrowIds));
    }
}
