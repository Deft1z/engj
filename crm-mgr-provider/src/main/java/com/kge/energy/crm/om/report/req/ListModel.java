package com.kge.energy.crm.om.report.req;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ListModel {
    /**
     * 合同id
     */
    private Integer ContractId;
    /**
     * 合同名字
     */
    private String ContractName;
    /**
     * 合同甲方名称
     */
    private String FirstParty;
    /**
     * 甲方联系人电话
     */
    private String FirstPartyContactsPhone;
    /**
     * 服务id
     */
    private String PrjId;
    /**
     * 服务名称
     */
    private String PrjName;
    /**
     * 服务单位
     */
    private String Dept;
    /**
     * 运维计划id
     */
    private String PlanId;
    /**
     * 运维计划编号
     */
    private String WorkCode;
    /**
     * 运维计划日期
     */
    private String WorkDate;
    /**
     * 专业明细
     */
    private String TaskName;
    /**
     * 施工任务
     */
    private String RiskRate;
    /**
     * 专业分类
     */
    private String WorkContent;
    /**
     * 合同甲方名称
     */
    private String CheckinId;
    /**
     * 问题备注
     */
    private String RemarkB;
    /**
     * 结论
     */
    private String RemarkC;
    /**
     * 照片
     */
    private ImageModel[] Attactments;

}
