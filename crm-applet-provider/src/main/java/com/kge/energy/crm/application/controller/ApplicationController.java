package com.kge.energy.crm.application.controller;

import com.kge.energy.crm.application.req.AppUnbindReq;
import com.kge.energy.crm.application.service.ApplicationService;
import com.kge.energy.crm.common.go.ConvertToGoFormats;
import com.kge.energy.crm.common.net.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

 /**
 * @description 小程序我的-业务系统接口
 * @author tangchenghui
 * @time 2024/7/29 11:36
 */
@RestController
@RequestMapping()
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    /**
     * @description 小程序我的->获取业务系统接口
     * @author tangchenghui
     * @date 2024/7/29 16:40
    */
    @ConvertToGoFormats
    @PostMapping("/application/applicationMrg/applicationList/currentUserAppListLoad")
    public CommonResponse<Object> getAppList() {
        return CommonResponse.suc(applicationService.getAppList());
    }

    /**
     * @description 小程序我的->解绑业务系统接口
     * @author tangchenghui
     * @date 2024/7/29 16:40
    */
    @ConvertToGoFormats
    @PostMapping("/application/bindingMgr/info/update")
    public CommonResponse<Object> unbindApp(@Validated @RequestBody AppUnbindReq appUnbindReq) {
        return CommonResponse.suc(applicationService.unbindApp(appUnbindReq));
    }
}
