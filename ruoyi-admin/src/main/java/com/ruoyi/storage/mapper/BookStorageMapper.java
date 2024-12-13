package com.ruoyi.storage.mapper;

import java.util.List;
import com.ruoyi.storage.domain.BookStorage;
import org.apache.ibatis.annotations.Param;

/**
 * 图书库存Mapper接口
 *
 * @author ruoyi
 * @date 2024-12-10
 */
public interface BookStorageMapper
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
     * 删除图书库存
     *
     * @param storageId 图书库存主键
     * @return 结果
     */
    public int deleteBookStorageByStorageId(Long storageId);

    /**
     * 批量删除图书库存
     *
     * @param storageIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBookStorageByStorageIds(Long[] storageIds);


    /**
     * 获取库存大于1的图书ID列表
     *
     * @return 结果
     */
    public List<Long> selectAvailableBookIds();

    /**
     * 获取某图书馆的图书ID列表
     * @param libraryId
     * @return
     */
    public List<Long> selectBookIdsByLibraryId(@Param("libraryId") Long libraryId);

    /**
     * 获取某图书的图书馆ID列表
     * @param bookId 图书id
     * @return
     */
    public List<Long> selectLibraryIdsByBookId(Long bookId);

    /**
     * 乐观锁解决超借问题
     * 根据图书馆ID、图书ID锁定库存记录
     * @param libraryId
     * @param bookId
     * @return
     */
    public Boolean reduceStockByLibraryIdAndBookId(@Param("libraryId")Long libraryId, @Param("bookId") Long bookId);

    /**
     * 查询某图书的所有图书馆存货之和
     * @param bookId
     * @return
     */
    public Long selectTotalStockByBookId(@Param("bookId") Long bookId);
}
