package com.kge.energy.crm.content.req;

import com.kge.energy.crm.common.page.PageReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class ModuleReq extends PageReq {
    // TODO 后续需要将map变为明确的参数，不建议使用map接收参数
    private Map<String, String> searchMap;
}
