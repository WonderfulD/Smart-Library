package com.ruoyi.library.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 图书馆信息对象 Libraries
 * 
 * @author ruoyi
 * @date 2024-03-12
 */
public class Libraries extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 图书馆ID */
    @Excel(name = "图书馆ID")
    private Long libraryId;

    /** 图书馆名称 */
    @Excel(name = "图书馆名称")
    private String name;

    /** 图书馆地址 */
    @Excel(name = "图书馆地址")
    private String address;

    /** 图书馆联系信息 */
    @Excel(name = "图书馆联系信息")
    private String contactInfo;

    public void setLibraryId(Long libraryId) 
    {
        this.libraryId = libraryId;
    }

    public Long getLibraryId() 
    {
        return libraryId;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setAddress(String address) 
    {
        this.address = address;
    }

    public String getAddress() 
    {
        return address;
    }
    public void setContactInfo(String contactInfo) 
    {
        this.contactInfo = contactInfo;
    }

    public String getContactInfo() 
    {
        return contactInfo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("libraryId", getLibraryId())
            .append("name", getName())
            .append("address", getAddress())
            .append("contactInfo", getContactInfo())
            .toString();
    }
}
