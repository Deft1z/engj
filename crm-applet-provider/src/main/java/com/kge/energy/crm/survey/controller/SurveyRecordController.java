package com.kge.energy.crm.survey.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.survey.req.SurveyRecordReq;
import com.kge.energy.crm.survey.resp.SurveyInitResp;
import com.kge.energy.crm.survey.resp.SurveyRecordResp;
import com.kge.energy.crm.survey.resp.SurveyResult;
import com.kge.energy.crm.survey.service.BSurveyRecordAnswerService;
import com.kge.energy.crm.survey.service.BSurveyRecordService;
import com.kge.energy.crm.survey.service.BSurveyService;
import com.kge.platform.framework.common.net.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 调查表单记录表(BSurveyRecord)Controller层
 *
 * @author zhengwenke
 * @since 2024-10-30 09:27:36
 */
@RestController
@RequestMapping("/api/v1/survey")
@Tag(name = "调查表单")
@RequiredArgsConstructor
public class SurveyRecordController {

    private final BSurveyService surveyService;

    private final BSurveyRecordService surveyRecordService;

    private final BSurveyRecordAnswerService surveyRecordAnswerService;

    @ApiOperationSupport(order = 1)
    @Operation(summary = "获取个人调查表单列表")
    @PostMapping("/record/getByPage")
    public CommonResult<PageResp<SurveyRecordResp>> getByPage(@RequestBody SurveyRecordReq req) {
        return CommonResult.suc(surveyRecordService.getByPage(req));
    }

    @ApiOperationSupport(order = 2)
    @Operation(summary = "查看个人调查表单详情")
    @Parameter(name = "id", description = "表单记录id", required = true, in = ParameterIn.QUERY)
    @GetMapping("/record/getById")
    public CommonResult<SurveyInitResp> getRecordById(@RequestParam(value = "id", required = true) Integer id) {
        return CommonResult.suc(surveyRecordService.getById(id));
    }

    @ApiOperationSupport(order = 3)
    @Operation(summary = "发起人查询新增可用的调查表单清单")
    @GetMapping("/getAll")
    public CommonResult<List<SurveyResult>> getAllSurvey() {
        return CommonResult.suc(surveyService.getAll());
    }

    @ApiOperationSupport(order = 4)
    @Operation(summary = "发起人新增调查表单初始化")
    @Parameter(name = "surveyCode", description = "表单编码", required = false, in = ParameterIn.QUERY)
    @GetMapping("/record/add")
    public CommonResult<SurveyInitResp> initSurvey(@RequestParam(value = "surveyCode", required = false) String surveyCode) {
        //暂时只有客户满意度调查表单 code=customer-satisfaction
        surveyCode = StringUtils.isBlank(surveyCode) ? "customer-satisfaction" : surveyCode;
        return CommonResult.suc(surveyService.getBySurveyCode(surveyCode));
    }

    @ApiOperationSupport(order = 5)
    @Operation(summary = "发起人保存/提交新增的调查表单")
    @PostMapping("/record/save")
    public CommonResult<Boolean> saveRecord(@RequestBody SurveyInitResp req) {
        return CommonResult.suc(surveyRecordService.save(req));
    }

    @ApiOperationSupport(order = 6)
    @Operation(summary = "发起人删除调查表单")
    @Parameter(name = "id", description = "表单记录id", in = ParameterIn.PATH)
    @PostMapping("/record/delete/{id}")
    public CommonResult<Boolean> delete(@PathVariable("id") Integer id) {
        return CommonResult.suc(surveyRecordService.delete(id));
    }

    @ApiOperationSupport(order = 7)
    @Operation(summary = "获取调查评价二维码(base64)")
    @Parameter(name = "id", description = "表单记录id", required = true, in = ParameterIn.QUERY)
    @PostMapping("/answer/qrcode")
    public CommonResult<String> getQrcode(@RequestParam(value = "id", required = true) Integer id) {
        return CommonResult.suc(surveyRecordService.getShareQrcode(id));
    }

    @ApiOperationSupport(order = 8)
    @Operation(summary = "受邀请人填写调查表单初始化")
    @Parameter(name = "recordId", description = "调查表单id", required = true, in = ParameterIn.QUERY)
    @GetMapping("/answer/add")
    public CommonResult<SurveyInitResp> initAnswer(@RequestParam(value = "recordId") Integer recordId) {
        return CommonResult.suc(surveyRecordAnswerService.initAnswer(recordId));
    }

    @ApiOperationSupport(order = 9)
    @Operation(summary = "受邀请人保存/提交填写完成的调查表单")
    @PostMapping("/answer/save")
    public CommonResult<Boolean> saveAnswer(@RequestBody SurveyInitResp req) {
        return CommonResult.suc(surveyRecordAnswerService.save(req));
    }

    @ApiOperationSupport(order = 10)
    @Operation(summary = "发起人确认完成受邀请人填写的调查表单")
    @PostMapping("/record/complete")
    public CommonResult<Boolean> completeRecord(@RequestBody SurveyInitResp req) {
        return CommonResult.suc(surveyRecordService.complete(req));
    }

}

