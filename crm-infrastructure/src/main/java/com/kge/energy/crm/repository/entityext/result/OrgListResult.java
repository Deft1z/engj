package com.kge.energy.crm.repository.entityext.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(name = "组织列表name", description = "组织列表对象")
public class OrgListResult {

    @Schema(description = "组织id")
    private Integer organizationId;

    @Schema(description = "上级组织id")
    private Integer parentOrganizationId;

    @Schema(description = "上级组织name")
    private String parentOrganizationName;

    @Schema(description = "组织层级")
    private Integer level;

    @Schema(description = "租户id")
    private Integer tenantId;

    @Schema(description = "组织名称")
    private String name;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "状态,0正常 1停用")
    private Integer status;

    @Schema(description = "创建时间")
    private String createTime;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "子节点")
    private List<OrgListResult> children;

}
