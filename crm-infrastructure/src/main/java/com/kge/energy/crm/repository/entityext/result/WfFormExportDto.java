package com.kge.energy.crm.repository.entityext.result;

import com.alibaba.excel.annotation.ExcelProperty;
import com.kge.energy.crm.easyexcel.CustomMerge;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 工单列表导出DTO类
 */
@Data
@Accessors(chain = true)
public class WfFormExportDto {

    @ExcelProperty("需求单号")
    @CustomMerge(isPk = true)
    private String code;

    @ExcelProperty("业务类型")
    @CustomMerge
    private String businessName;

    @ExcelProperty("用电容量")
    @CustomMerge
    private String voltageLevel;

    @ExcelProperty("所属地区")
    @CustomMerge
    private String area;

    @ExcelProperty("客户地址")
    @CustomMerge
    private String detailedAddress;

    @ExcelProperty("联系人")
    @CustomMerge
    private String customerName;

    @ExcelProperty("联系电话")
    @CustomMerge
    private String mobile;

    @ExcelProperty("起单时间")
    @CustomMerge
    private String timeSubmit;

    @ExcelProperty("接单时间")
    @CustomMerge
    private String timeReception;

    @ExcelProperty("结单时间")
    @CustomMerge
    private String timeFinished;

    @ExcelProperty("工单状态")
    @CustomMerge
    private String status;

    @ExcelProperty("对接公司")
    @CustomMerge
    private String orgName;

    @ExcelProperty("是否已签订合同")
    @CustomMerge
    private String contractSignedFlag;

    @ExcelProperty("合同编号")
    private String contractCode;

    @ExcelProperty("合同名称")
    private String contractName;

    @ExcelProperty("合同签订时间")
    private String contractSigningTime;

    @ExcelProperty("合同金额")
    private String contractAmount;

    @ExcelProperty("备注")
    private String remark;


}

