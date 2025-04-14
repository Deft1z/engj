package com.kge.energy.crm.repository.entityext.result;

import com.alibaba.excel.annotation.ExcelProperty;
import com.kge.energy.crm.easyexcel.CustomMerge;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PromoteUserDataExportDto {
    @ExcelProperty("用户姓名")
    @CustomMerge
    private String realname;
    @ExcelProperty("用户手机")
    @CustomMerge
    private String mobile;
    @ExcelProperty("组织")
    @CustomMerge
    private String orgName;
    @ExcelProperty("推广用户open_id")
    @CustomMerge
    private String promoteUserOpenId;
    @ExcelProperty("推广用户姓名")
    @CustomMerge
    private String promoteUserName;
    @ExcelProperty("推广用户手机")
    @CustomMerge
    private String promoteUserPhone;
    @ExcelProperty("推广用户创建时间")
    @CustomMerge
    private String promoteUserCreateTime;
}
