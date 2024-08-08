package com.kge.energy.crm.repository.entityext.result;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PatrolRecordResp {
    private Integer reportId; // 运维报告记录id


    private Integer formId; // 工单id


    private String serviceCode; // 合同编号


    private String patrolRecordCode; // 报告码


    private Integer reportFileId; // 报告附带文件


    private Integer reportResult; // 1-有合同；0-无合同


    private String operator; // 操作员


    private String operationTime; // 操作时间


    private String filepath; // 文件路径
}
