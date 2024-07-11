package com.kge.energy.crm.order.controller;

import com.kge.energy.crm.common.go.ConvertToGoFormats;
import com.kge.energy.crm.common.net.CommonResponse;
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

/**
 * @author wangjihua
 */
@RestController
@RequestMapping("/workMgrBack/contractBack")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    /**
     * 获取合同
     */
    @ConvertToGoFormats
    @PostMapping("/form")
    public CommonResponse<List<ContractResp>> form(@Validated @RequestBody ContractReq req) {

        return CommonResponse.suc(contractService.form(req));
    }
}
