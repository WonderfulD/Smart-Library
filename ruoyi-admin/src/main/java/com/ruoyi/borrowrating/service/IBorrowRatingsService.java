package com.ruoyi.borrowrating.service;

import java.util.List;
import com.ruoyi.borrowrating.domain.BorrowRatings;

/**
 * 借阅评分Service接口
 * 
 * @author ruoyi
 * @date 2024-03-30
 */
public interface IBorrowRatingsService 
{
    /**
     * 查询借阅评分
     * 
     * @param borrowId 借阅评分主键
     * @return 借阅评分
     */
    public BorrowRatings selectBorrowRatingsByBorrowId(Long borrowId);

    /**
     * 查询借阅评分列表
     * 
     * @param borrowRatings 借阅评分
     * @return 借阅评分集合
     */
    public List<BorrowRatings> selectBorrowRatingsList(BorrowRatings borrowRatings);

    /**
     * 新增借阅评分
     * 
     * @param borrowRatings 借阅评分
     * @return 结果
     */
    public int insertBorrowRatings(BorrowRatings borrowRatings);

    /**
     * 修改借阅评分
     * 
     * @param borrowRatings 借阅评分
     * @return 结果
     */
    public int updateBorrowRatings(BorrowRatings borrowRatings);

    /**
     * 批量删除借阅评分
     * 
     * @param borrowIds 需要删除的借阅评分主键集合
     * @return 结果
     */
    public int deleteBorrowRatingsByBorrowIds(Long[] borrowIds);

    /**
     * 删除借阅评分信息
     * 
     * @param borrowId 借阅评分主键
     * @return 结果
     */
    public int deleteBorrowRatingsByBorrowId(Long borrowId);
}
