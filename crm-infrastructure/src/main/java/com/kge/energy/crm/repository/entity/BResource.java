package com.kge.energy.crm.repository.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 资源表(BResource)实体类
 *
 * @author wangjihua
 * @since 2024-07-30 16:47:28
 */
@Data
@Accessors(chain = true)
public class BResource {

    @TableId(type = IdType.AUTO)
    private Integer resourceId;

    /**
     * 父资源
     */
    private Integer parentResourceId;

    /**
     * 资源名称
     */
    private String resourceName;

    /**
     * 资源编码
     */
    private String resourceCode;

    /**
     * 资源类型：menu、button
     */
    private String resourceType;

    /**
     * 重构后停用
     */
    private Integer referResourceId;

    /**
     * 0，1，2，3,路由三级，重构后停用
     */
    private Integer level;

    /**
     * 子系统、板块、模块、功能块，重构后停用
     */
    private String type;

    /**
     * 与真实路由对应，重构后停用
     */
    private String name;

    /**
     * 管理后台资源名称，重构后停用
     */
    private String displayName;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 资源地址
     */
    private String path;

    /**
     * 资源地址类型：system、file、app、link
     */
    private String pathType;

    /**
     * 图标编码
     */
    private String iconCode;

    /**
     * 图标文件路径
     */
    private String iconFilePath;

    /**
     * 菜单状态（0正常 1停用）
     */
    private Integer status;

    /**
     * 系统类型：applet、mgr
     */
    private String systemType;

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

    /**
     * 租户id
     */
    private Integer tenantId;
}

