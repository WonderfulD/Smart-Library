package com.ruoyi.rate.service;

import java.util.List;
import com.ruoyi.rate.domain.BookRatings;

/**
 * 藏书评分Service接口
 *
 * @author ruoyi
 * @date 2024-03-20
 */
public interface IBookRatingsService
{
    /**
     * 查询藏书评分
     *
     * @param ratingId 藏书评分主键
     * @return 藏书评分
     */
    public BookRatings selectBookRatingsByRatingId(Long ratingId);

    /**
     * 查询藏书评分列表
     *
     * @param bookRatings 藏书评分
     * @return 藏书评分集合
     */
    public List<BookRatings> selectBookRatingsList(BookRatings bookRatings);

    /**
     * 新增藏书评分
     *
     * @param bookRatings 藏书评分
     * @return 结果
     */
    public int insertBookRatings(BookRatings bookRatings);

    /**
     * 修改藏书评分
     *
     * @param bookRatings 藏书评分
     * @return 结果
     */
    public int updateBookRatings(BookRatings bookRatings);

    /**
     * 批量删除藏书评分
     *
     * @param ratingIds 需要删除的藏书评分主键集合
     * @return 结果
     */
    public int deleteBookRatingsByRatingIds(Long[] ratingIds);

    /**
     * 删除藏书评分信息
     *
     * @param ratingId 藏书评分主键
     * @return 结果
     */
    public int deleteBookRatingsByRatingId(Long ratingId);
}
