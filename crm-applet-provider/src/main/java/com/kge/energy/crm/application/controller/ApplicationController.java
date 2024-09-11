package com.kge.energy.crm.application.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kge.energy.crm.application.req.AppBindReq;
import com.kge.energy.crm.application.req.AppDetailReq;
import com.kge.energy.crm.application.req.AppTokenReq;
import com.kge.energy.crm.application.req.AppUnbindReq;
import com.kge.energy.crm.application.resp.AppDetailResp;
import com.kge.energy.crm.application.service.ApplicationService;
import com.kge.energy.crm.common.go.ConvertToGoFormats;
import com.kge.energy.crm.repository.entityext.result.AppListResult;
import com.kge.platform.framework.common.net.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * @author tangchenghui
 * @description 小程序我的-业务系统接口
 * @time 2024/7/29 11:36
 */
@RestController
@RequestMapping("/application")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    /**
     * @description 小程序我的->获取业务系统接口
     * @author tangchenghui
     * @date 2024/7/29 16:40
     */
    @ConvertToGoFormats
    @PostMapping("/applicationMrg/applicationList/currentUserAppListLoad")
    public CommonResult<List<AppListResult>> getAppList() {
        return CommonResult.suc(applicationService.getAppList());
    }

    /**
     * @description 小程序我的->解绑业务系统接口
     * @author tangchenghui
     * @date 2024/7/29 16:40
     */
    @ConvertToGoFormats
    @PostMapping("/bindingMgr/info/update")
    public CommonResult<Boolean> unbindApp(@Validated @RequestBody AppUnbindReq appUnbindReq) {
        return CommonResult.suc(applicationService.unbindApp(appUnbindReq));
    }

    @ConvertToGoFormats
    @PostMapping("/bindingMgr/info/insert")
    public CommonResult<Boolean> bindApp(@Validated @RequestBody AppBindReq appBindReq) {
        return CommonResult.suc(applicationService.bindApp(appBindReq));
    }

    @ConvertToGoFormats
    @PostMapping("/tokenMgr/info/load")
    public CommonResult<Object> getAppToken(@Validated @RequestBody AppTokenReq appTokenReq) throws NoSuchAlgorithmException, JsonProcessingException {
        return applicationService.getAppToken(appTokenReq);
    }

    @Operation(summary = "获取APP详情信息")
    @PostMapping("/detail")
    public CommonResult<List<AppDetailResp>> getAppDetail(@Validated @RequestBody AppDetailReq appTokenReq) {
        return CommonResult.suc(applicationService.getAppDetail(appTokenReq));
    }
}
