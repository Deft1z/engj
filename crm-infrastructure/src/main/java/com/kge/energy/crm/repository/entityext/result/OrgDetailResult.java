package com.kge.energy.crm.repository.entityext.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 组织详情(BOrganizationDetail)实体类
 */
@Data
@Accessors(chain = true)
@Schema(description = "组织详情对象")
public class OrgDetailResult {


    @Schema(description = "组织ID")
    private Integer organizationId;


    @Schema(description = "组织全称")
    private String fullName;


    @Schema(description = "服务商页面的组织类型标签")
    private String label;

    @Schema(description = "组织图片文件路径")
    private String filepath;


    @Schema(description = "组织介绍")
    private String introduce;


    @Schema(description = "组织地址")
    private String address;


    @Schema(description = "组织服务类型")
    private Integer serviceType;


    @Schema(description = "组织业务范围")
    private String serviceRange;

}


