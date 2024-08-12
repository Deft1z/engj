package com.kge.energy.crm.user.resp;

import com.kge.energy.crm.common.page.PageReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(name = "用户列表响应参数", description = "用户列表响应参数")
public class UserListResp extends PageReq {

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
}
