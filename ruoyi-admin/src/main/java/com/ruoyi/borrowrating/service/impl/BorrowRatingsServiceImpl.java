package com.ruoyi.borrowrating.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.borrowrating.mapper.BorrowRatingsMapper;
import com.ruoyi.borrowrating.domain.BorrowRatings;
import com.ruoyi.borrowrating.service.IBorrowRatingsService;

/**
 * 借阅评分Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-03-30
 */
@Service
public class BorrowRatingsServiceImpl implements IBorrowRatingsService 
{
    @Autowired
    private BorrowRatingsMapper borrowRatingsMapper;

    /**
     * 查询借阅评分
     * 
     * @param borrowId 借阅评分主键
     * @return 借阅评分
     */
    @Override
    public BorrowRatings selectBorrowRatingsByBorrowId(Long borrowId)
    {
        return borrowRatingsMapper.selectBorrowRatingsByBorrowId(borrowId);
    }

    /**
     * 查询借阅评分列表
     * 
     * @param borrowRatings 借阅评分
     * @return 借阅评分
     */
    @Override
    public List<BorrowRatings> selectBorrowRatingsList(BorrowRatings borrowRatings)
    {
        return borrowRatingsMapper.selectBorrowRatingsList(borrowRatings);
    }

    /**
     * 新增借阅评分
     * 
     * @param borrowRatings 借阅评分
     * @return 结果
     */
    @Override
    public int insertBorrowRatings(BorrowRatings borrowRatings)
    {
        return borrowRatingsMapper.insertBorrowRatings(borrowRatings);
    }

    /**
     * 修改借阅评分
     * 
     * @param borrowRatings 借阅评分
     * @return 结果
     */
    @Override
    public int updateBorrowRatings(BorrowRatings borrowRatings)
    {
        return borrowRatingsMapper.updateBorrowRatings(borrowRatings);
    }

    /**
     * 批量删除借阅评分
     * 
     * @param borrowIds 需要删除的借阅评分主键
     * @return 结果
     */
    @Override
    public int deleteBorrowRatingsByBorrowIds(Long[] borrowIds)
    {
        return borrowRatingsMapper.deleteBorrowRatingsByBorrowIds(borrowIds);
    }

    /**
     * 删除借阅评分信息
     * 
     * @param borrowId 借阅评分主键
     * @return 结果
     */
    @Override
    public int deleteBorrowRatingsByBorrowId(Long borrowId)
    {
        return borrowRatingsMapper.deleteBorrowRatingsByBorrowId(borrowId);
    }
}
