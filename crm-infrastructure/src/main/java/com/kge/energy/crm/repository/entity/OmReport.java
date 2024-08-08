package com.kge.energy.crm.repository.entity;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.*;

/**
 * om_report(OmReport)实体类
 *
 * @author wangjihua
 * @since 2024-07-03 20:38:23
 */
@Data
@Accessors(chain = true)
public class OmReport {

    /**
     * 运维报告
     */
    @TableId(type = IdType.AUTO)
    private Integer reportId; 

    /**
     * 合同ID
     */
    private Integer formId; 

    /**
     * 合同码
     */
    private String serviceCode; 

    /**
     * 报告码
     */
    private String patrolRecordCode; 

    /**
     * 报告附带文件
     */
    private Integer reportFileId; 

    /**
     * 1-有合同；0-无合同
     */
    private Integer reportResult; 

    /**
     * 操作员
     */
    private String operator; 

    /**
     * 操作时间
     */
    private LocalDateTime operationTime; 

    /**
     * 软删除标识
     */
    private Integer flag; 

    /**
     * 创建用户ID
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer createUserId; 

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
     * 文件真实名字
     */
    private String reportFileName; 
}

