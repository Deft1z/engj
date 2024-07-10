package com.kge.energy.crm.org.controller;

import com.kge.energy.crm.common.net.CommonResponse;
import com.kge.energy.crm.org.resp.OrgDictResp;
import com.kge.energy.crm.org.service.OrgService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author wangjihua
 */
@RestController
@RequestMapping("/baseDataBack/organizationBackMrg")
@RequiredArgsConstructor
public class OrgController {

    private final OrgService orgService;

    /**
     * 获取组织字典
     */
    @PostMapping("/getOrgDictList")
    public CommonResponse<List<OrgDictResp>> getOrgDictList() {

        return CommonResponse.suc(orgService.getOrgDictList());
    }

}
