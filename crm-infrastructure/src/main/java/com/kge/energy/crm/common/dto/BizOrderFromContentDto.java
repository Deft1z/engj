package com.kge.energy.crm.common.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhengwenke
 * @date 2024/10/12 15:02
 */
@Data
@Accessors(chain = true)
public class BizOrderFromContentDto {

    /**
     * 业务工单编号
     */
    private String code;

    /**
     * 业务名称
     */
    private String businessName;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 客户手机号
     */
    private String mobile;

    /**
     * 所在地区
     */
    private String area;

    /**
     * 详细地址
     */
    private String detailedAddress;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 电压等级
     */
    private String voltageLevel;

    /**
     * 用电容量
     */
    private String electricityCapacity;

    /**
     * 备注
     */
    private String remark;

}
