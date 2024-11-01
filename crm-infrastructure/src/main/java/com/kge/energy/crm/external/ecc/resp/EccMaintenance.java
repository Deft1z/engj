package com.kge.energy.crm.external.ecc.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class EccMaintenance {
    /**
     * 合同id
     */
    private String contractId;
    /**
     * 合同名字
     */
    private String contractName;
    /**
     * 合同甲方名称
     */
    private String firstParty;
    /**
     * 甲方联系人电话
     */
    private String firstPartyContactsPhone;
    /**
     * 服务id
     */
    private String prjId;
    /**
     * 服务名称
     */
    private String prjName;
    /**
     * 服务单位
     */
    private String dept;
    /**
     * 运维计划id
     */
    private String planId;
    /**
     * 运维计划编号
     */
    private String workCode;
    /**
     * 运维计划日期
     */
    private String workDate;
    /**
     * 专业明细
     */
    private String taskName;
    /**
     * 施工任务
     */
    private String riskRate;
    /**
     * 专业分类
     */
    private String workContent;
    /**
     * 合同甲方名称
     */
    private String checkinId;
    /**
     * 问题备注
     */
    private String remarkB;
    /**
     * 结论
     */
    private String remarkC;
    /**
     * 照片
     */
    private List<EccAttachment> attactments;

}
