package com.ruoyi.library.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.library.mapper.LibrariesMapper;
import com.ruoyi.library.domain.Libraries;
import com.ruoyi.library.service.ILibrariesService;

/**
 * 图书馆信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-03-12
 */
@Service
public class LibrariesServiceImpl implements ILibrariesService 
{
    @Autowired
    private LibrariesMapper librariesMapper;

    /**
     * 查询图书馆信息
     * 
     * @param libraryId 图书馆信息主键
     * @return 图书馆信息
     */
    @Override
    public Libraries selectLibrariesByLibraryId(Long libraryId)
    {
        return librariesMapper.selectLibrariesByLibraryId(libraryId);
    }

    /**
     * 查询图书馆信息列表
     * 
     * @param libraries 图书馆信息
     * @return 图书馆信息
     */
    @Override
    public List<Libraries> selectLibrariesList(Libraries libraries)
    {
        return librariesMapper.selectLibrariesList(libraries);
    }

    /**
     * 新增图书馆信息
     * 
     * @param libraries 图书馆信息
     * @return 结果
     */
    @Override
    public int insertLibraries(Libraries libraries)
    {
        return librariesMapper.insertLibraries(libraries);
    }

    /**
     * 修改图书馆信息
     * 
     * @param libraries 图书馆信息
     * @return 结果
     */
    @Override
    public int updateLibraries(Libraries libraries)
    {
        return librariesMapper.updateLibraries(libraries);
    }

    /**
     * 批量删除图书馆信息
     * 
     * @param libraryIds 需要删除的图书馆信息主键
     * @return 结果
     */
    @Override
    public int deleteLibrariesByLibraryIds(Long[] libraryIds)
    {
        return librariesMapper.deleteLibrariesByLibraryIds(libraryIds);
    }

    /**
     * 删除图书馆信息信息
     * 
     * @param libraryId 图书馆信息主键
     * @return 结果
     */
    @Override
    public int deleteLibrariesByLibraryId(Long libraryId)
    {
        return librariesMapper.deleteLibrariesByLibraryId(libraryId);
    }
}
