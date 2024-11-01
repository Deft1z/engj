package com.kge.energy.crm.easyexcel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ExportReq {

    @Schema(description = "导出类型：0 excel 1 pdf")
    private Integer exportType = 0;

}