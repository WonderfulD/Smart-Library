package com.ruoyi.library.service;

import java.util.List;
import com.ruoyi.library.domain.Libraries;

/**
 * 图书馆信息Service接口
 * 
 * @author ruoyi
 * @date 2024-03-12
 */
public interface ILibrariesService 
{
    /**
     * 查询图书馆信息
     * 
     * @param libraryId 图书馆信息主键
     * @return 图书馆信息
     */
    public Libraries selectLibrariesByLibraryId(Long libraryId);

    /**
     * 查询图书馆信息列表
     * 
     * @param libraries 图书馆信息
     * @return 图书馆信息集合
     */
    public List<Libraries> selectLibrariesList(Libraries libraries);

    /**
     * 新增图书馆信息
     * 
     * @param libraries 图书馆信息
     * @return 结果
     */
    public int insertLibraries(Libraries libraries);

    /**
     * 修改图书馆信息
     * 
     * @param libraries 图书馆信息
     * @return 结果
     */
    public int updateLibraries(Libraries libraries);

    /**
     * 批量删除图书馆信息
     * 
     * @param libraryIds 需要删除的图书馆信息主键集合
     * @return 结果
     */
    public int deleteLibrariesByLibraryIds(Long[] libraryIds);

    /**
     * 删除图书馆信息信息
     * 
     * @param libraryId 图书馆信息主键
     * @return 结果
     */
    public int deleteLibrariesByLibraryId(Long libraryId);
}
