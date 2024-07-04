package com.kge.energy.crm.repository.entity;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.*;

/**
 * 用户租户关系(RUserTenant)实体类
 *
 * @author wangjihua
 * @since 2024-07-03 20:38:23
 */
@Data
@Accessors(chain = true)
public class RUserTenant {

    /**
     * 用户机构归属表（停用）
     */
    @TableId(type = IdType.AUTO)
    private Integer userTenantId; 

    /**
     * 用户ID
     */
    private Integer userId; 

    /**
     * 组织ID
     */
    private Integer organizationId; 

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

