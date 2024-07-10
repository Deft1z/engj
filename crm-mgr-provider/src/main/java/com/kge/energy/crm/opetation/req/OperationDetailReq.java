package com.kge.energy.crm.opetation.req;

import lombok.Data;

@Data
public class OperationDetailReq {
    private String operationTime;

    /**
     * 报告时间（编辑）
     */
    private String serviceCode;

    /**
     * 报告人 （编辑）
     */
    private String operator;

    /**
     * 文件名（编辑）
     */
    private String reportFileName;

    /**
     * 文件（编辑）
     */
    private int reportFileId;

    /**
     * 运维记录号
     */
    private String patrolRecordCode;

    /**
     * 报告结论
     */
    private int reportResult;
}
