package com.kge.energy.crm.repository.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * w_complain_flow 投诉反馈流程(WComplainFlow)实体类
 *
 * @author wangjihua
 * @since 2024-07-03 20:38:23
 */
@Data
@Accessors(chain = true)
public class WComplainFlow {

    @TableId(type = IdType.AUTO)
    private Integer complainFlowId; 

    private Integer complainId; 

    private LocalDateTime timeAction; 

    private Integer userId; 

    private String actionType; 

    private String actionContent; 

    private String status; 

    private String subStatus; 

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

