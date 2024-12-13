package com.ruoyi.storage.service.impl;

import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.storage.mapper.BookStorageMapper;
import com.ruoyi.storage.domain.BookStorage;
import com.ruoyi.storage.service.IBookStorageService;

/**
 * 图书库存Service业务层处理
 *
 * @author ruoyi
 * @date 2024-12-10
 */
@Service
public class BookStorageServiceImpl implements IBookStorageService
{
    @Autowired
    private BookStorageMapper bookStorageMapper;

    /**
     * 查询图书库存
     *
     * @param storageId 图书库存主键
     * @return 图书库存
     */
    @Override
    public BookStorage selectBookStorageByStorageId(Long storageId)
    {
        return bookStorageMapper.selectBookStorageByStorageId(storageId);
    }

    /**
     * 查询图书库存列表
     *
     * @param bookStorage 图书库存
     * @return 图书库存
     */
    @Override
    public List<BookStorage> selectBookStorageList(BookStorage bookStorage)
    {
        return bookStorageMapper.selectBookStorageList(bookStorage);
    }

    /**
     * 新增图书库存
     *
     * @param bookStorage 图书库存
     * @return 结果
     */
    @Override
    public int insertBookStorage(BookStorage bookStorage)
    {
        return bookStorageMapper.insertBookStorage(bookStorage);
    }

    /**
     * 修改图书库存
     *
     * @param bookStorage 图书库存
     * @return 结果
     */
    @Override
    public int updateBookStorage(BookStorage bookStorage)
    {
        return bookStorageMapper.updateBookStorage(bookStorage);
    }

    /**
     * 批量删除图书库存
     *
     * @param storageIds 需要删除的图书库存主键
     * @return 结果
     */
    @Override
    public int deleteBookStorageByStorageIds(Long[] storageIds)
    {
        return bookStorageMapper.deleteBookStorageByStorageIds(storageIds);
    }

    /**
     * 删除图书库存信息
     *
     * @param storageId 图书库存主键
     * @return 结果
     */
    @Override
    public int deleteBookStorageByStorageId(Long storageId)
    {
        return bookStorageMapper.deleteBookStorageByStorageId(storageId);
    }

    /**
     * 获取库存大于1的图书ID列表
     * @return
     */
    @Override
    public List<Long> selectAvailableBookIDsList() {
        // 通过库存表查询出库存合计大于1的所有book_id
        return bookStorageMapper.selectAvailableBookIds();
    }

    /**
     * 根据图书馆ID获取图书ID列表
     * @param libraryId 图书馆id
     * @return
     */
    @Override
    public List<Long> selectBookIdsByLibraryId(Long libraryId) {
        return bookStorageMapper.selectBookIdsByLibraryId(libraryId);
    }

    /**
     * 根据图书馆ID和图书ID列表删除库存记录
     * @param libraryId
     * @param bookIds
     */
    @Override
    public void deleteBookStorageByLibraryIdAndBookIds(Long libraryId, Long[] bookIds) {
        for (Long bookId : bookIds) {
            BookStorage bookStorage = new BookStorage();
            bookStorage.setLibraryId(libraryId);
            bookStorage.setBookId(bookId);
            List<BookStorage> bookStorages = selectBookStorageList(bookStorage);
            deleteBookStorageByStorageId(bookStorages.get(0).getStorageId());
        }
    }

    /**
     * 获取某图书的图书馆ID列表
     * @param bookId 图书id
     * @return
     */
    @Override
    public List<Long> selectLibraryIdsByBookId(Long bookId) {
        return bookStorageMapper.selectLibraryIdsByBookId(bookId);
    }


    /**
     * 使用乐观锁扣除某图书馆某本书的库存（库存减一）
     * @param libraryId
     * @param bookId
     * @return
     */
    @Override
    public Boolean borrowOneBookWithOLock(Long libraryId, Long bookId) {
        return bookStorageMapper.reduceStockByLibraryIdAndBookId(libraryId, bookId);
    }

    @Override
    public Long selectTotalStockByBookId(Long bookId) {
        return bookStorageMapper.selectTotalStockByBookId(bookId);
    }
}
