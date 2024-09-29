package com.kge.energy.crm.repository.entityext.result;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 工单列表导出DTO类
 */
@Data
@Accessors(chain = true)
public class WfFormExportDto {
    @ExcelIgnore
    private Integer formId;
    @ExcelIgnore
    private Integer formTypeId;
    @ExcelIgnore
    private Integer formMetaId;
    @ExcelIgnore
    private String content;
    @ExcelProperty("需求单号")
    private String code;
    @ExcelProperty("所属地区")
    private String area;
    @ExcelProperty("详细地址")
    private String detailedAddress;
    @ExcelProperty("业务类型")
    private String businessName;
    @ExcelProperty("用电容量")
    private String voltageLevel;
    @ExcelProperty("联系人")
    private String customerName;
    @ExcelProperty("联系电话")
    private String mobile;
    @ExcelProperty("对接公司")
    private String orgName;
    @ExcelProperty("工单状态")
    private String status;
    @ExcelProperty("起单时间")
    private String timeSubmit;
    @ExcelProperty("接单时间")
    private String timeReception;
    @ExcelProperty("结单时间")
    private String timeFinished;
    @ExcelProperty("是否已签合同")
    private String ifContractSigned;
    @ExcelProperty("合同名称")
    private String contractName;
    @ExcelProperty("合同签订时间")
    private String contractSignTime;
    @ExcelProperty("合同金额")
    private String contractAmount;
    @ExcelProperty("备注")
    private String remark;

    @ExcelIgnore
    private String subStatus;
    @ExcelIgnore
    private String modifyTime;
    @ExcelIgnore
    private Integer currentOrgId;
    @ExcelIgnore
    private Integer currentRoleId;
    @ExcelIgnore
    private Integer flag;
    @ExcelIgnore
    private Integer createUserId;
    @ExcelIgnore
    private Integer modifyUserId;
    @ExcelIgnore
    private String companyName;
    @ExcelIgnore
    private String electricityCapacity;
    @ExcelIgnore
    private String userStatus;
    @ExcelIgnore
    private Integer organizationId;
    @ExcelIgnore
    private String realname;
}

