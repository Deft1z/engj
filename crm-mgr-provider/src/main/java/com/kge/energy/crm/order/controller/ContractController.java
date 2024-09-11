package com.kge.energy.crm.order.controller;

import com.kge.energy.crm.common.go.ConvertToGoFormats;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.order.req.ContractReq;
import com.kge.energy.crm.order.req.WxUserWorkOrderReq;
import com.kge.energy.crm.order.req.contract.CreateContractReq;
import com.kge.energy.crm.order.req.contract.UpdateProjectTimeReq;
import com.kge.energy.crm.order.resp.ContractResp;
import com.kge.energy.crm.order.service.ContractService;
import com.kge.energy.crm.repository.entityext.result.ContractResult;
import com.kge.platform.framework.common.net.CommonResult;
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
    public CommonResult<List<ContractResp>> form(@Validated @RequestBody ContractReq req) {
        return CommonResult.suc(contractService.form(req));
    }

    /**
     * 小程序客户 -> 获取合同
     */
    @ConvertToGoFormats
    @PostMapping("/contractPageByUserIdLoad")
    public CommonResult<PageResp<ContractResult>> contractPageByUserIdLoad(@Validated @RequestBody WxUserWorkOrderReq req) {
        return CommonResult.suc(contractService.contractPageByUserIdLoad(req));
    }

    /**
     * 添加合同
     */
    @ConvertToGoFormats
    @PostMapping("/form/insert")
    public CommonResult<Object> contractAdd(@Validated @RequestBody CreateContractReq req) {
        return contractService.contractAdd(req);
    }

    /**
     * 填写合同开工，竣工时间
     */
    @ConvertToGoFormats
    @PostMapping("/projectTime/update")
    public CommonResult<Object> projectTimeEdit(@Validated @RequestBody UpdateProjectTimeReq req) {
        return contractService.projectTimeEdit(req);
    }
}
