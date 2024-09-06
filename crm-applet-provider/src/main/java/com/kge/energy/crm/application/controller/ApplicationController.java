package com.kge.energy.crm.application.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kge.energy.crm.application.req.AppBindReq;
import com.kge.energy.crm.application.req.AppDetailReq;
import com.kge.energy.crm.application.req.AppTokenReq;
import com.kge.energy.crm.application.req.AppUnbindReq;
import com.kge.energy.crm.application.resp.AppDetailResp;
import com.kge.energy.crm.application.service.ApplicationService;
import com.kge.energy.crm.common.go.ConvertToGoFormats;
import com.kge.energy.crm.common.net.CommonResponse;
import com.kge.energy.crm.repository.entityext.result.AppListResult;
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
    public CommonResponse<List<AppListResult>> getAppList() {
        return CommonResponse.suc(applicationService.getAppList());
    }

    /**
     * @description 小程序我的->解绑业务系统接口
     * @author tangchenghui
     * @date 2024/7/29 16:40
     */
    @ConvertToGoFormats
    @PostMapping("/bindingMgr/info/update")
    public CommonResponse<Boolean> unbindApp(@Validated @RequestBody AppUnbindReq appUnbindReq) {
        return CommonResponse.suc(applicationService.unbindApp(appUnbindReq));
    }

    @ConvertToGoFormats
    @PostMapping("/bindingMgr/info/insert")
    public CommonResponse<Boolean> bindApp(@Validated @RequestBody AppBindReq appBindReq) {
        return CommonResponse.suc(applicationService.bindApp(appBindReq));
    }

    @ConvertToGoFormats
    @PostMapping("/tokenMgr/info/load")
    public CommonResponse<Object> getAppToken(@Validated @RequestBody AppTokenReq appTokenReq) throws NoSuchAlgorithmException, JsonProcessingException {
        return applicationService.getAppToken(appTokenReq);
    }

    @Operation(summary = "获取APP详情信息")
    @PostMapping("/detail")
    public CommonResponse<List<AppDetailResp>> getAppDetail(@Validated @RequestBody AppDetailReq appTokenReq) {
        return CommonResponse.suc(applicationService.getAppDetail(appTokenReq));
    }
}
