package com.kge.energy.crm.workflow.controller;

import com.kge.energy.crm.common.go.ConvertToGoFormats;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.complain.controller.ComplainController;
import com.kge.energy.crm.workflow.req.ConsultingAddReq;
import com.kge.energy.crm.workflow.req.ConsultingUpdateReq;
import com.kge.energy.crm.workflow.req.WfFormFlowReq;
import com.kge.energy.crm.workflow.req.WfFormReq;
import com.kge.energy.crm.workflow.resp.WfFormFlowResp;
import com.kge.energy.crm.workflow.resp.WfFormResp;
import com.kge.energy.crm.workflow.service.ConsultingService;
import com.kge.platform.framework.common.net.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/workMgr/consulting")
@Tag(name = "业务工单")
@RequiredArgsConstructor
public class ConsultingController {

    private final ConsultingService consultingService;

    private final ComplainController complainController;

    @Operation(summary = "创建业务工单")
    @PostMapping(value = "/opt/insert")
    @ConvertToGoFormats
    public CommonResult<Boolean> save(@RequestBody @Valid ConsultingAddReq req) {
        return CommonResult.suc(consultingService.save(req));
    }

    @Operation(summary = "获取工单列表")
    @PostMapping(value = "/getFormPage")
    @ConvertToGoFormats
    public CommonResult getFormPage(@RequestBody @Valid WfFormReq req) {
        //原go项目接口使用工单类型id区分业务工单和投诉工单，但响应的数据字段完全不同，为了接口文档能准确生成请求和响应参数，拆分原go业务代码逻辑，此接口的目的单纯只为兼容原接口的地址
        //业务工单formTypeId=2调用 ConsultingController.getByPage
        //投诉工单formTypeId=1调用 ComplainController.getByPage
        if (req.getFormTypeId().equals(2)) {
            return complainController.getByPage(req);
        } else {
            return this.getByPage(req);
        }
    }

    @Operation(summary = "分页获取工单列表")
    @PostMapping(value = "/getByPage")
    @ConvertToGoFormats
    public CommonResult<PageResp<WfFormResp>> getByPage(@RequestBody WfFormReq req) {
        return CommonResult.suc(consultingService.getByPage(req));
    }

    @Operation(summary = "获取当前工单流程的流转情况")
    @PostMapping(value = "/getFlowByFormId")
    @ConvertToGoFormats
    public CommonResult<List<WfFormFlowResp>> getFlowByFormId(@RequestBody WfFormFlowReq req) {
        return CommonResult.suc(consultingService.getFlowByFormId(req));
    }

    @Operation(summary = "工单操作")
    @PostMapping(value = "/opt/update")
    @ConvertToGoFormats
    public CommonResult<Boolean> update(@RequestBody @Valid ConsultingUpdateReq req) {
        return CommonResult.suc(consultingService.update(req));
    }

}
