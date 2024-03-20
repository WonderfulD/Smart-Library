package com.ruoyi.rate.mapper;

import java.util.List;
import com.ruoyi.rate.domain.BookRatings;

/**
 * 藏书评分Mapper接口
 * 
 * @author ruoyi
 * @date 2024-03-20
 */
public interface BookRatingsMapper 
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
     * 删除藏书评分
     * 
     * @param ratingId 藏书评分主键
     * @return 结果
     */
    public int deleteBookRatingsByRatingId(Long ratingId);

    /**
     * 批量删除藏书评分
     * 
     * @param ratingIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBookRatingsByRatingIds(Long[] ratingIds);
}
