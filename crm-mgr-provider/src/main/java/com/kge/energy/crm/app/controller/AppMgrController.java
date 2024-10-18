package com.kge.energy.crm.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kge.energy.crm.app.req.AppMgrListAddReq;
import com.kge.energy.crm.app.req.AppMgrListReq;
import com.kge.energy.crm.app.req.AppMgrListUpdateReq;
import com.kge.energy.crm.app.req.InfoUnbindReq;
import com.kge.energy.crm.app.service.AppMgrService;
import com.kge.energy.crm.common.go.ConvertToGoFormats;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.repository.entityext.result.AppMgrListResult;
import com.kge.platform.framework.common.net.CommonResult;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class AppMgrController {

    private final AppMgrService appMgrService;

    @ConvertToGoFormats
    @PostMapping("/baseDataBack/appMgr/applicationList/load")
    public CommonResult<PageResp<AppMgrListResult>> appListLoad(@RequestBody AppMgrListReq req) {
        return CommonResult.suc(appMgrService.appListLoad(req));
    }

    @ConvertToGoFormats
    @PostMapping("/applicationBack/applicationBackMrg/appWeb/insert")
    public CommonResult<Boolean> appFormInsert(@Validated @RequestBody AppMgrListAddReq req) {
        return CommonResult.suc(appMgrService.appFormInsert(req));
    }

    @ConvertToGoFormats
    @PostMapping("/applicationBack/applicationBackMrg/appWeb/update")
    public CommonResult<Boolean> appFormUpdate(@Validated @RequestBody AppMgrListUpdateReq req) {
        return CommonResult.suc(appMgrService.appFormUpdate(req));
    }

    @ConvertToGoFormats
    @PostMapping("/applicationBack/bindingMgr/bind/update")
    public CommonResult<Boolean> infoUnbind(@Validated @RequestBody InfoUnbindReq req) throws NoSuchAlgorithmException, JsonProcessingException {
        return CommonResult.suc(appMgrService.infoUnbind(req));
    }

}
