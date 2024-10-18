package com.kge.energy.crm.org.controller;

import com.kge.energy.crm.common.go.ConvertToGoFormats;
import com.kge.energy.crm.org.req.AddOrgReq;
import com.kge.energy.crm.org.req.DeleteOrgReq;
import com.kge.energy.crm.org.req.OrgQueryReq;
import com.kge.energy.crm.org.req.UpdateOrgReq;
import com.kge.energy.crm.org.resp.OrgDictResp;
import com.kge.energy.crm.org.resp.OrgTreeResp;
import com.kge.energy.crm.org.service.OrgService;
import com.kge.energy.crm.repository.entityext.result.OrgListResult;
import com.kge.platform.framework.common.net.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author wangjihua
 */
@Tag(name = "组织管理")
@RestController
@RequestMapping("/org")
@RequiredArgsConstructor
public class OrgController {

    private final OrgService orgService;

    /**
     * 获取组织字典
     */
    @ConvertToGoFormats
    @PostMapping("/getOrgDictList")
    public CommonResult<List<OrgDictResp>> getOrgDictList() {
        return CommonResult.suc(orgService.getOrgDictList());
    }

    @Operation(summary = "组织列表")
    @PostMapping("/getOrgList")
    public CommonResult<List<OrgListResult>> list(@Validated @RequestBody OrgQueryReq req) {
        return CommonResult.suc(orgService.selectList(req));
    }

    @Operation(summary = "组织树")
    @PostMapping("/getOrgTree")
    public CommonResult<OrgTreeResp> getOrgTree(@Validated @RequestBody OrgQueryReq req) {
        return CommonResult.suc(orgService.getOrgTree(req));
    }

    @Operation(summary = "新增组织")
    @PostMapping("/addOrg")
    public CommonResult<Boolean> add(@Validated @RequestBody AddOrgReq req) {
        return CommonResult.suc(orgService.add(req));
    }

    @Operation(summary = "更新组织")
    @PostMapping("/updateOrg")
    public CommonResult<Boolean> update(@Validated @RequestBody UpdateOrgReq updateOrgReq) {
        return CommonResult.suc(orgService.update(updateOrgReq));
    }

    @Operation(summary = "删除组织")
    @PostMapping("/deleteOrg")
    public CommonResult<Boolean> delete(@Validated @RequestBody DeleteOrgReq deleteOrgReq) {
        return CommonResult.suc(orgService.delete(deleteOrgReq));
    }

}
