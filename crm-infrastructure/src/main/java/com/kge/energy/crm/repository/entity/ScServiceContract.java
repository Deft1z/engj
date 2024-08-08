package com.kge.energy.crm.repository.entity;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.*;

/**
 * sc_service_contract 服务合同(ScServiceContract)实体类
 *
 * @author wangjihua
 * @since 2024-07-03 20:38:24
 */
@Data
@Accessors(chain = true)
public class ScServiceContract {

    /**
     * 服务合同表
     */
    @TableId(type = IdType.AUTO)
    private Integer serviceContractId; 

    /**
     * 表单表
     */
    private Integer formId; 

    /**
     * 文件表
     */
    private Integer fileId; 

    /**
     * 客户单位
     */
    private String company; 

    /**
     * 合同名称
     */
    private String name; 

    /**
     * 合同编号
     */
    private String code; 

    /**
     * 合同金额
     */
    private Double amount; 

    /**
     * 实施地址
     */
    private String implAddress; 

    /**
     * 实施详细地址
     */
    private String implAddressDetail; 

    /**
     * 合同签订时间
     */
    private LocalDateTime signingTime; 

    /**
     * 服务开始时间
     */
    private LocalDateTime serviceStartTime; 

    /**
     * 服务结束时间
     */
    private LocalDateTime serviceEndTime; 

    /**
     * 项目开始时间
     */
    private LocalDateTime projectStartTime; 

    /**
     * 项目结束时间
     */
    private LocalDateTime projectEndTime; 

    /**
     * 项目编号
     */
    private String projectCode; 

    /**
     * 服务单位
     */
    private Integer serviceUnit; 

    /**
     * 合同内容
     */
    private String content; 

    /**
     * 合同负责人
     */
    private Integer owner; 

    /**
     * 现场项目经理
     */
    private Integer pm; 

    /**
     * 合同状态
     */
    private String status; 

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

