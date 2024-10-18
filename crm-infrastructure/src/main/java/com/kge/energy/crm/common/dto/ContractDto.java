package com.kge.energy.crm.common.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 服务合同
 *
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
public class ContractDto {

    /**
     * 合同签订标识
     */
    @ExcelProperty("是否已签订合同")
    private String contractSignedFlag;

    /**
     * 合同编号
     */
    @ExcelProperty("合同编号")
    private String contractCode;

    /**
     * 合同名称
     */
    @ExcelProperty("合同名称")
    private String contractName;

    /**
     * 合同签订时间
     */
    @ExcelProperty("合同签订时间")
    private String contractSigningTime;

    /**
     * 合同金额
     */
    @ExcelProperty("合同金额")
    private String contractAmount;

}
