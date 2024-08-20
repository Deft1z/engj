package com.kge.energy.crm.log.req;

import com.kge.energy.crm.common.page.PageReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author wangjihua
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(name = "操作日志列表请求参数", description = "操作日志列表请求参数")
public class SysOperateLogListReq extends PageReq {

    @Schema(description = "租户ID")
    private Integer tenantId;

    @Schema(description = "操作模块: 0-租户，1-组织，2-用户，3-角色，4-菜单，5-资源接口，6-用户家园应用")
    private Integer operateModule;

    @Schema(description = "操作人名称")
    private String operateName;
}
