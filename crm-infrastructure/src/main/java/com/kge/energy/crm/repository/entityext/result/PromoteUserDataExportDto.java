package com.kge.energy.crm.repository.entityext.result;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PromoteUserDataExportDto {

    @ExcelProperty("用户姓名")
    private String realname;

    @ExcelProperty("用户手机")
    private String mobile;

    @ExcelProperty("组织")
    private String orgName;

    @ExcelProperty("推广用户open_id")
    private String promoteUserOpenId;

    @ExcelProperty("推广用户姓名")
    private String promoteUserName;

    @ExcelProperty("推广用户手机")
    private String promoteUserPhone;

    @ExcelProperty("推广用户创建时间")
    private String promoteUserCreateTime;
}
