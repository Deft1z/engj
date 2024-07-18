package com.kge.energy.crm.app.controller;

import com.kge.energy.crm.app.req.AppBindingListReq;
import com.kge.energy.crm.app.req.WxUserAppReq;
import com.kge.energy.crm.app.resp.AppDetailUserResc;
import com.kge.energy.crm.app.resp.WxUserAppResp;
import com.kge.energy.crm.app.service.AppService;
import com.kge.energy.crm.common.go.ConvertToGoFormats;
import com.kge.energy.crm.common.net.CommonResponse;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.order.req.ContractReq;
import com.kge.energy.crm.order.resp.ContractResp;
import com.kge.energy.crm.order.service.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public CommonResponse<List<WxUserAppResp>> getAppListByUserId(@Validated @RequestBody WxUserAppReq req) {
        return CommonResponse.suc(appService.contractPageByUserIdLoad(req));
    }
    /**   含有分页功能
    public CommonResponse<PageResp<WxUserAppResp>> getAppListByUserId(@Validated @RequestBody WxUserAppReq req) {
        return CommonResponse.suc(appService.contractPageByUserIdLoad(req));
    }
     */

    @ConvertToGoFormats
    @PostMapping("/appUnbindingListLoad")
    public CommonResponse<List<AppDetailUserResc>> appUnbindingListLoad(@Validated @RequestBody AppBindingListReq req) {
        return CommonResponse.suc(appService.appUnbindingListLoad(req));
    }
}
