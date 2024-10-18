package com.kge.energy.crm.user.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
@Schema(name = "用户详情响应参数", description = "用户详情响应参数")
public class UserDetailResp {

    @Schema(description = "租户ID")
    private Integer tenantId;

    @Schema(description = "租户名称")
    private String tenantName;

    @Schema(description = "部门ID")
    private Integer organizationId;

    @Schema(description = "部门名称")
    private String organizationName;

    @Schema(description = "用户ID")
    private Integer userId;

    @Schema(description = "用户名称")
    private String name;

    @Schema(description = "真实姓名")
    private String realname;

    @Schema(description = "手机号码")
    private String mobile;

    @Schema(description = "帐号状态（0正常 1停用）")
    private Integer status;

    @Schema(description = "备注")
    private String remark;
}
