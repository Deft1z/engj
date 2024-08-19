package com.kge.energy.crm.app.controller;

import com.kge.energy.crm.app.req.ForceBindingReq;
import com.kge.energy.crm.app.service.ExternalBindService;
import com.kge.energy.crm.common.go.ConvertToGoFormats;
import com.kge.energy.crm.common.net.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class ExternalBindController {

    private final ExternalBindService externalBindService;

    @ConvertToGoFormats
    @PostMapping("/externalBack/application/forceAccountBinding")
    public CommonResponse<Object> forceBinding(@Validated @RequestBody ForceBindingReq forceBindingReq){
        return externalBindService.forceBinding(forceBindingReq);
    }

}
