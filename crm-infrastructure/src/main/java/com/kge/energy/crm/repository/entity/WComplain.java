package com.kge.energy.crm.repository.entity;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.*;

/**
 * 投诉反馈(WComplain)实体类
 *
 * @author wangjihua
 * @since 2024-07-03 20:38:25
 */
@Data
@Accessors(chain = true)
public class WComplain {

    /**
     * 投诉表
     */
    @TableId(type = IdType.AUTO)
    private Integer complainId; 

    /**
     * 用户ID
     */
    private Integer userId; 

    /**
     * 被投诉单位id
     */
    private Integer organizationId; 

    /**
     * 1 问题；2 投诉
     */
    private Integer typef; 

    /**
     * 进度慢；质量差；态度恶劣
     */
    private String subject; 

    /**
     * 内容
     */
    private String content; 

    /**
     * 投诉公司
     */
    private String company; 

    /**
     * 合同内容
     */
    private String contacts; 

    /**
     * 手机
     */
    private String phone; 

    /**
     * 地址
     */
    private String address; 

    /**
     * 0 待处理；1 正在处理；2 完成
     */
    private Integer status; 

    /**
     * 反馈
     */
    private String feedback; 

    /**
     * 处理用户ID
     */
    private Integer processUserId; 

    /**
     * 处理时间
     */
    private LocalDateTime processTime; 

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

