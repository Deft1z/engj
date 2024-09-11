package com.kge.energy.crm.app.controller;

import com.kge.energy.crm.app.req.AppBindingListReq;
import com.kge.energy.crm.app.req.WxUserAppReq;
import com.kge.energy.crm.app.resp.AppDetailUserResp;
import com.kge.energy.crm.app.resp.WxUserAppResp;
import com.kge.energy.crm.app.service.AppService;
import com.kge.energy.crm.common.go.ConvertToGoFormats;
import com.kge.energy.crm.repository.entity.BApp;
import com.kge.platform.framework.common.net.CommonResult;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/applicationBack/applicationBackMrg")
@RequiredArgsConstructor
public class AppController {
    private final AppService appService;

    /**
     * 小程序客户 -> 绑定第三方应用
     */
    @ConvertToGoFormats
    @PostMapping("/UserAppListLoad")
    public CommonResult<List<WxUserAppResp>> getAppListByUserId(@Validated @RequestBody WxUserAppReq req) {
        return CommonResult.suc(appService.contractPageByUserIdLoad(req));
    }

    /**
     * 小程序客户 -> 获取客户未绑定的应用列表
     */
    @ConvertToGoFormats
    @PostMapping("/appUnbindingListLoad")
    public CommonResult<List<AppDetailUserResp>> appUnbindingListLoad(@Validated @RequestBody AppBindingListReq req) {
        return CommonResult.suc(appService.appUnbindingListLoad(req));
    }

    /**
     * 绑定管理 -> 获取所有应用列表
     */
    @ConvertToGoFormats
    @GetMapping("/list")
    public CommonResult<List<BApp>> list() {
        return CommonResult.suc(appService.list());
    }


}
