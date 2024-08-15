package com.kge.energy.crm.repository.entityext.result;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wangjihua
 */
@NoArgsConstructor
@Data
public class ContractResult {

    /**
     * 服务合同表id
     */
    private Integer serviceContractId;

    /**
     * 工单id
     */
    private Integer formId;

    /**
     * 文件id
     */
    private Integer fileId;

    /**
     * 合同名称
     */
    private String name;

    /**
     * 公司
     */
    private String company;

    /**
     * 合同编号
     */
    private String code;

    /**
     * 合同金额
     */
    private String amount;

    /**
     * 项目编号
     */
    private String projectCode;

    /**
     * 项目开始时间
     */
    private String projectStartTime;

    /**
     * 项目结束时间
     */
    private String projectEndTime;

    /**
     * 合同签订时间
     */
    private String signingTime;

    /**
     * 服务开始时间
     */
    private String serviceStartTime;

    /**
     * 服务结束时间
     */
    private String serviceEndTime;

    /**
     * 服务公司的id
     */
    private Integer serviceUnit;

    /**
     * 状态
     */
    private String status;

    /**
     * 服务提供商
     */
    private String serviceProvider;

    /**
     * 工单详细信息json
     */
    private String content;

    /**
     * 发起工单人id
     */
    private Integer owner;

    /**
     * 发起工单人的名称
     */
    private String ownerName;

    /**
     * 负责人名称
     */
    private String pmName;

    /**
     * 负责人id
     */
    private Integer pm;

    /**
     * 发起工单人的电话
     */
    private String ownerMobile;

    /**
     * 负责人电话
     */
    private String pmMobile;

    /**
     *
     */
    private Integer flag;

    /**
     *
     */
    private Integer createUserId;

    /**
     *
     */
    private Integer modifyUserId;

    /**
     *
     */
    private String remark;

    /**
     * 满意度
     */
    private Integer satisfaction;

    /**
     * 评价留言
     */
    private String evaluate;

    /**
     * 组织id
     */
    private Integer organizationId;
}
