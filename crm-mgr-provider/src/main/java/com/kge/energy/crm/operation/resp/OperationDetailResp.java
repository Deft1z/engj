package com.kge.energy.crm.operation.resp;

import lombok.Data;

import java.time.LocalDate;

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
    private LocalDate operationTime;

    /**
     * 文件真实名字
     */
    private String reportFileName;

    /**
     * 文件路径
     */
    private String filepath;
}
