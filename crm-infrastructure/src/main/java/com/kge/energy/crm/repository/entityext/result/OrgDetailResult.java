package com.kge.energy.crm.repository.entityext.result;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 组织详情(BOrganizationDetail)实体类
 */
@Data
@Accessors(chain = true)
@TableName(autoResultMap = true)
public class OrgDetailResult {

    /**
     * 组织ID
     */
    private Integer organizationId;

    /**
     * 组织全称
     */
    private String fullName;

    /**
     * 服务商页面的组织类型标签
     */
    private String label;

    /**
     * 组织图片文件路径
     */
    private String filepath;


    /**
     * 组织介绍
     */
    private String introduce;

    /**
     * 组织地址
     */
    private String address;

    /**
     * 组织服务类型
     */
    private Integer serviceType;

    /**
     * 组织业务范围
     */
    private String serviceRange;

}


