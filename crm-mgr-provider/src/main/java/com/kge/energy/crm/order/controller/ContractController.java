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
import com.kge.energy.crm.workOrder.req.ServiceContractAddReq;
import com.kge.energy.crm.workOrder.req.ServiceContractReq;
import com.kge.energy.crm.workOrder.req.ServiceContractUpdateProjectTimeReq;
import com.kge.energy.crm.workOrder.resp.ServiceContractResp;
import com.kge.energy.crm.workOrder.service.ServiceContractDomainService;
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

    private final ServiceContractDomainService serviceContractDomainService;

    /**
     * 获取合同
     */
    @ConvertToGoFormats
    @PostMapping("/form")
    public CommonResult<List<ServiceContractResp>> form(@Validated @RequestBody ServiceContractReq req) {
        return CommonResult.suc(serviceContractDomainService.getServiceContractList(req));
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
    public CommonResult<Boolean> contractAdd(@Validated @RequestBody ServiceContractAddReq req) {
        return CommonResult.suc(serviceContractDomainService.addServiceContract(req));
    }

    /**
     * 填写合同开工，竣工时间
     */
    @ConvertToGoFormats
    @PostMapping("/projectTime/update")
    public CommonResult<Object> projectTimeEdit(@Validated @RequestBody ServiceContractUpdateProjectTimeReq req) {
        return CommonResult.suc(serviceContractDomainService.updateProjectTime(req));
    }
}
