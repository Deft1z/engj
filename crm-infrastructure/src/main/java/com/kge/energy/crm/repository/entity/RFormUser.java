package com.kge.energy.crm.repository.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * r_form_user 表单用户表(RFormUser)实体类
 *
 * @author wangjihua
 * @since 2024-07-03 20:38:24
 */
@Data
@Accessors(chain = true)
public class RFormUser {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * Wf_form主键
     */
    private Integer formId;

    /**
     * 用户ID
     */
    private Integer userId;
}

