package com.kge.energy.crm.sso.controller;

import com.kge.energy.crm.sso.req.SSOReq;
import com.kge.energy.crm.sso.resp.SSOResp;
import com.kge.energy.crm.sso.service.SSOService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SSOController {

    private final SSOService ssoService;

    @PostMapping("/external/sso/auth")
    public SSOResp auth(@Validated @RequestBody SSOReq req) {
        return ssoService.auth(req);
    }
}
