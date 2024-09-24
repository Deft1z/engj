package com.kge.energy.crm.permission.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
@Schema(name = "获取数据权限配置列表响应", description = "获取数据权限配置列表响应对象")
public class DataPermissionListResp {

    @Schema(description = "关联配置ID")
    private Integer id;

    @Schema(description = "业务功能配置ID")
    private Integer bizFunctionId;

    @Schema(description = "角色ID集合")
    private Integer roleId;

    @Schema(description = "数据权限范围类型：0-所有，1-租户，2-集团，3-公司，4-部门，5-个人")
    private Integer dataRangeType;

    @Schema(description = "优先级，越大越高")
    private Integer priority;
}
