package com.kge.energy.crm.organization.controller;

import com.kge.energy.crm.common.go.ConvertToGoFormats;
import com.kge.energy.crm.common.net.CommonResponse;
import com.kge.energy.crm.organization.req.OrgReq;
import com.kge.energy.crm.organization.service.OrgService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class OrgController {

    private final OrgService orgService;

    /**
     * @description 小程序端获取服务商接口
     * @author tangchenghui
     * @date 2024/7/26 17:56
    */
    @ConvertToGoFormats
    @PostMapping("/baseData/company/list")
    public CommonResponse<Object> getCompanyList(@RequestBody OrgReq orgReq){
        return CommonResponse.suc(orgService.getCompanyList(orgReq));
    }
}
