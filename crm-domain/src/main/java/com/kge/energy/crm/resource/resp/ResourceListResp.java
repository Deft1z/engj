package com.kge.energy.crm.resource.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
public class ResourceListResp {

    private List<ResourceBean> resources;
}
