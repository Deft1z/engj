package com.kge.energy.crm.repository.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * 机构表详情(BOrganizationDetail)实体类
 *
 */
@Data
@Accessors(chain = true)
@TableName(autoResultMap = true)
public class BOrganizationDetail {

    /**
     * 组织Id
     */
    @TableId
    private Integer organizationId;

    /**
     * 服务商页面的组织标签
     */
    private String label;

    /**
     * 图片文件路径
     */
    private String filepath;

    /**
     * 组织全称
     */
    private String fullName;

    /**
     * 组织服务类型
     */
    private Integer serviceType;

    /**
     * 组织业务范围
     */
    private String serviceRange;

    /**
     * 组织介绍
     */
    private String introduce;

    /**
     * 组织地址
     */
    private String address;
}

