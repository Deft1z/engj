package com.kge.energy.crm.repository.entityext.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
@Schema(name = "租户列表name", description = "租户列表对象")
public class TenantListResult {

    @Schema(description = "租户id")
    private Integer id;

    @Schema(description = "租户名")
    private String name;

    @Schema(description = "联系人")
    private String contactName;

    @Schema(description = "联系人手机")
    private String contactMobile;

    @Schema(description = "租户状态（0正常 1停用）")
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
