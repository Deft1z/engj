package com.kge.energy.crm.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kge.energy.crm.app.req.AppMgrListAddReq;
import com.kge.energy.crm.app.req.AppMgrListReq;
import com.kge.energy.crm.app.req.AppMgrListUpdateReq;
import com.kge.energy.crm.app.req.InfoUnbindReq;
import com.kge.energy.crm.app.service.AppMgrService;
import com.kge.energy.crm.common.go.ConvertToGoFormats;
import com.kge.energy.crm.common.net.CommonResponse;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.repository.entityext.result.AppMgrListResult;
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
    public CommonResponse<PageResp<AppMgrListResult>> appListLoad(@RequestBody AppMgrListReq req){
        return CommonResponse.suc(appMgrService.appListLoad(req));
    }

    @ConvertToGoFormats
    @PostMapping("/applicationBack/applicationBackMrg/appWeb/insert")
    public CommonResponse<Boolean> appFormInsert(@Validated @RequestBody AppMgrListAddReq req){
        return CommonResponse.suc(appMgrService.appFormInsert(req));
    }

    @ConvertToGoFormats
    @PostMapping("/applicationBack/applicationBackMrg/appWeb/update")
    public CommonResponse<Boolean> appFormUpdate(@Validated @RequestBody AppMgrListUpdateReq req){
        return CommonResponse.suc(appMgrService.appFormUpdate(req));
    }

    @ConvertToGoFormats
    @PostMapping("/applicationBack/bindingMgr/bind/update")
    public CommonResponse<Boolean> infoUnbind(@Validated @RequestBody InfoUnbindReq req) throws NoSuchAlgorithmException, JsonProcessingException {
        return CommonResponse.suc(appMgrService.infoUnbind(req));
    }

}
