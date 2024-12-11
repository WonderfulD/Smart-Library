package com.ruoyi.storage.service;

import com.ruoyi.storage.domain.BookStorage;

import java.util.List;

/**
 * 图书库存Service接口
 *
 * @author ruoyi
 * @date 2024-12-10
 */
public interface IBookStorageService
{
    /**
     * 查询图书库存
     *
     * @param storageId 图书库存主键
     * @return 图书库存
     */
    public BookStorage selectBookStorageByStorageId(Long storageId);

    /**
     * 查询图书库存列表
     *
     * @param bookStorage 图书库存
     * @return 图书库存集合
     */
    public List<BookStorage> selectBookStorageList(BookStorage bookStorage);

    /**
     * 新增图书库存
     *
     * @param bookStorage 图书库存
     * @return 结果
     */
    public int insertBookStorage(BookStorage bookStorage);

    /**
     * 修改图书库存
     *
     * @param bookStorage 图书库存
     * @return 结果
     */
    public int updateBookStorage(BookStorage bookStorage);

    /**
     * 批量删除图书库存
     *
     * @param storageIds 需要删除的图书库存主键集合
     * @return 结果
     */
    public int deleteBookStorageByStorageIds(Long[] storageIds);

    /**
     * 删除图书库存信息
     *
     * @param storageId 图书库存主键
     * @return 结果
     */
    public int deleteBookStorageByStorageId(Long storageId);

    /**
     * 获取库存大于1的图书ID列表
     * @return 图书id列表
     */
    public List<Long> selectAvailableBookIDsList();

    /**
     * 获取某图书馆的图书ID列表
     * @param libraryId 图书馆id
     * @return 图书id列表
     */
    public List<Long> selectBookIdsByLibraryId(Long libraryId);

    public void deleteBookStorageByLibraryIdAndBookIds(Long libraryId, Long[] bookIds);

    /**
     * 获取某图书的图书馆ID列表
     * @param bookId 图书id
     * @return
     */
    public List<Long> selectLibraryIdsByBookId(Long bookId);
}
