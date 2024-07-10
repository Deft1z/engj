package com.kge.energy.crm.opetation.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OperationDetailResp {

    /**
     * 运维报告
     */
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
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime operationTime;

    /**
     * 文件真实名字
     */
    private String reportFileName;

    /**
     * 文件路径
     */
    private String filepath;
}
