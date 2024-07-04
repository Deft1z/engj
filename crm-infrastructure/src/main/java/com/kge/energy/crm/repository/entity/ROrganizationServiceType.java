package com.kge.energy.crm.repository.entity;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.*;

/**
 * 公司服务清单(ROrganizationServiceType)实体类
 *
 * @author wangjihua
 * @since 2024-07-03 20:38:24
 */
@Data
@Accessors(chain = true)
public class ROrganizationServiceType {

    /**
     * 公司服务关系id
     */
    @TableId(type = IdType.AUTO)
    private Integer organizationFormTypeId; 

    /**
     * 公司
     */
    private Integer organizationId; 

    /**
     * 服务
     */
    private Integer formTypeId; 

    /**
     * 排序
     */
    private Integer sort; 

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

