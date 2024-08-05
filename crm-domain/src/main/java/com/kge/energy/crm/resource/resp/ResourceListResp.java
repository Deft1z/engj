package com.kge.energy.crm.resource.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
@Schema(name = "菜单列表name", description = "菜单列表对象")
public class ResourceListResp {

    private List<ResourceBean> resources;
}
