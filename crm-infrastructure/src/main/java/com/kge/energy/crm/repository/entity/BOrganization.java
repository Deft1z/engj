package com.kge.energy.crm.repository.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.kge.energy.crm.repository.entityext.result.OrganizationParameter;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 机构表(BOrganization)实体类
 *
 * @author wangjihua
 * @since 2024-07-03 20:38:23
 */
@Data
@Accessors(chain = true)
@TableName(autoResultMap = true)
public class BOrganization {

    /**
     * 集团及二级公司组织表
     */
    @TableId(type = IdType.AUTO)
    private Integer organizationId; 

    /**
     * 上一级组织ID
     */
    private Integer parentOrganizationId; 

    /**
     * 停用
     */
    private Integer regionId; 

    /**
     * 停用
     */
    private Integer userTenantId; 

    /**
     * 集团总部；电力建设；工程咨询设计；科技服务与智慧能源；物业运营；城建建设；
     */
    private String type; 

    /**
     * 总共两层
     */
    private Integer level; 

    /**
     * 停用
     */
    private String authCode; 

    /**
     * 为集团以及旗下16家子公司名称
     */
    private String name; 

    /**
     * 纬度
     */
    private String lat; 

    /**
     * 经度
     */
    private String lng; 

    /**
     * 停用
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private OrganizationParameter parameter;

    /**
     * 停用
     */
    private String variables; 

    /**
     * 停用
     */
    private String referSource; 

    /**
     * 停用
     */
    private String referId; 

    /**
     * 软删除标识
     */
    private Integer flag; 

    /**
     * 创建用户ID
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer createUserId; 

    /**
     * 创建时间
     */
    @TableField(insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createTime;

    /**
     * 修改用户ID
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer modifyUserId; 

    /**
     * 修改时间
     */
    @TableField(insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime modifyTime;

    /**
     * 备注
     */
    private String remark; 
}

