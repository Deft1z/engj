package com.kge.energy.crm.repository.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 表单(WfForm)实体类
 *
 * @author wangjihua
 * @since 2024-07-03 20:38:24
 */
@Data
@Accessors(chain = true)
public class WfForm {

    @TableId(type = IdType.AUTO)
    private Integer formId; 

    private Integer formTypeId; 

    private Integer formMetaId; 

    private String content; 

    private String status; 

    private String subStatus; 

    private LocalDateTime timeSubmit; 

    private LocalDateTime timeReception; 

    private LocalDateTime timeFinished; 

    private Integer currentRoleId; 

    private Integer currentOrgId; 

    private Integer flag; 

    @TableField(fill = FieldFill.INSERT)
    private Integer createUserId; 

    @TableField(insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createTime; 

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer modifyUserId; 

    @TableField(insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime modifyTime; 

    private String remark;

    /**
     * 租户id
     */
    private Integer tenantId;
}

