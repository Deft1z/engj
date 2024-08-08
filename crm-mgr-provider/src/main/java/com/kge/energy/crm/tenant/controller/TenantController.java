package com.kge.energy.crm.tenant.controller;

import com.kge.energy.crm.common.net.CommonResponse;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.tenant.req.AddTenantReq;
import com.kge.energy.crm.tenant.req.DeleteTenantReq;
import com.kge.energy.crm.tenant.req.UpdateTenantReq;
import com.kge.energy.crm.tenant.resp.TenantListResp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wangjihua
 */
@Tag(name = "租户管理")
@RestController
@RequestMapping("/tenant")
@RequiredArgsConstructor
public class TenantController {

    @Operation(summary = "租户列表")
    @GetMapping("/list")
    public CommonResponse<PageResp<TenantListResp>> list(@Validated @RequestBody QueryTenantReq req) {
        return null;
    }

    @Operation(summary = "新增租户")
    @PostMapping("/add")
    public CommonResponse<Boolean> add(@Validated @RequestBody AddTenantReq req) {
        return null;
    }

    @Operation(summary = "编辑租户")
    @PostMapping("/update")
    public CommonResponse<Boolean> update(@Validated @RequestBody UpdateTenantReq req) {
        return null;
    }

    @Operation(summary = "删除租户")
    @PostMapping("/delete")
    public CommonResponse<Boolean> update(@Validated @RequestBody DeleteTenantReq req) {
        return null;
    }
}
