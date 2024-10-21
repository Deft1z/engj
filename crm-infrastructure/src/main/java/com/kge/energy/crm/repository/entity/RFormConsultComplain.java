package com.kge.energy.crm.repository.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * r_form_consult_complain 咨询投诉工单表(RFormConsultComplain)实体类
 *
 * @author wangjihua
 * @since 2024-07-03 20:38:24
 */
@Data
@Accessors(chain = true)
public class RFormConsultComplain {

    /**
     * 工单/合同-投诉对应表
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 工单ID，工单1对多投诉
     */
    private Integer consultId;

    /**
     * 投诉ID
     */
    private Integer complainId;

    /**
     * 租户id
     */
    private Integer tenantId;
}

