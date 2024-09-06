package com.kge.energy.crm.organization.controller;

import com.kge.energy.crm.common.go.ConvertToGoFormats;
import com.kge.energy.crm.common.net.CommonResponse;
import com.kge.energy.crm.external.ecc.resp.EccOrgResp;
import com.kge.energy.crm.external.ecc.service.EccService;
import com.kge.energy.crm.organization.req.OrgReq;
import com.kge.energy.crm.organization.resp.OrgDictResp;
import com.kge.energy.crm.organization.resp.OrgResp;
import com.kge.energy.crm.organization.service.OrgService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class OrgController {

    private final OrgService orgService;

    private final EccService eccService;

    /**
     * @description 小程序端获取服务商接口
     * @author tangchenghui
     * @date 2024/7/26 17:56
     */
    @ConvertToGoFormats
    @PostMapping("/baseData/company/list")
    public CommonResponse<List<OrgResp>> getCompanyList(@RequestBody OrgReq orgReq) {
        return CommonResponse.suc(orgService.getCompanyList(orgReq));
    }

    /**
     * 获取每个公司的组织机构Id和服务类型
     */
    @PostMapping("/baseData/organizationMrg/orgDictList/load")
    public CommonResponse<List<OrgDictResp>> orgDictList() {
        return CommonResponse.suc(orgService.orgDictList());
    }

    @PostMapping("/baseData/getEccOrgList")
    public CommonResponse<List<EccOrgResp>> getEccOrgList() {
        return CommonResponse.suc(eccService.getEccOrgList());
    }
}
