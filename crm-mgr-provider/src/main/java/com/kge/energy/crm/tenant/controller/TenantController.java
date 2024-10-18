package com.kge.energy.crm.tenant.controller;

import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.repository.entityext.result.TenantListForOrgResult;
import com.kge.energy.crm.repository.entityext.result.TenantListResult;
import com.kge.energy.crm.tenant.req.AddTenantReq;
import com.kge.energy.crm.tenant.req.DeleteTenantReq;
import com.kge.energy.crm.tenant.req.QueryTenantReq;
import com.kge.energy.crm.tenant.req.UpdateTenantReq;
import com.kge.energy.crm.tenant.service.TenantService;
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
@Tag(name = "租户管理")
@RestController
@RequestMapping("/tenant")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    @Operation(summary = "租户列表")
    @PostMapping("/list")
    public CommonResult<PageResp<TenantListResult>> list(@Validated @RequestBody QueryTenantReq req) {
        return CommonResult.suc(tenantService.selectPage(req));
    }

    @Operation(summary = "新增租户")
    @PostMapping("/add")
    public CommonResult<Boolean> add(@Validated @RequestBody AddTenantReq req) {
        return CommonResult.suc(tenantService.add(req));
    }

    @Operation(summary = "编辑租户")
    @PostMapping("/update")
    public CommonResult<Boolean> update(@Validated @RequestBody UpdateTenantReq req) {
        return CommonResult.suc(tenantService.update(req));
    }

    @Operation(summary = "删除租户")
    @PostMapping("/delete")
    public CommonResult<Boolean> delete(@Validated @RequestBody DeleteTenantReq req) {
        return CommonResult.suc(tenantService.delete(req));
    }

    @Operation(summary = "获取租户筛选列表")
    @PostMapping("/getTenantDictList")
    public CommonResult<List<TenantListForOrgResult>> getTenantDictList() {
        return CommonResult.suc(tenantService.getTenantDictList());
    }
}
