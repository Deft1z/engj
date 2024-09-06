package com.kge.energy.crm.org.resp;

import com.kge.energy.crm.repository.entityext.result.OrgListResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@Schema(name = "组织列表响应参数", description = "组织列表响应参数")
public class OrgTreeResp {

    @Schema(description = "组织树")
    List<OrgListResult> orgTree;

}
