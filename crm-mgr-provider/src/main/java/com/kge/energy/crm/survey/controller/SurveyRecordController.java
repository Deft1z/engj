package com.kge.energy.crm.survey.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.common.util.ExcelUtils;
import com.kge.energy.crm.survey.req.SurveyRecordExcelReq;
import com.kge.energy.crm.survey.req.SurveyRecordReq;
import com.kge.energy.crm.survey.resp.SurveyInitResp;
import com.kge.energy.crm.survey.resp.SurveyRecordResp;
import com.kge.energy.crm.survey.resp.SurveyResult;
import com.kge.energy.crm.survey.service.BSurveyRecordService;
import com.kge.energy.crm.survey.service.BSurveyService;
import com.kge.platform.framework.common.exception.ServiceException;
import com.kge.platform.framework.common.net.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
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
    public CommonResult<SurveyInitResp> initSurvey(@RequestParam(value = "surveyCode", required = false, defaultValue = "customer-satisfaction") String surveyCode) {
        //暂时只有客户满意度调查表单 code=customer-satisfaction
        return CommonResult.suc(surveyService.getBySurveyCode(surveyCode));
    }

    @ApiOperationSupport(order = 5)
    @Operation(summary = "发起人保存/提交新增的调查表单")
    @PostMapping("/record/save")
    public CommonResult<SurveyInitResp> saveRecord(@RequestBody SurveyInitResp req) {
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
    @GetMapping("/record/template/get")
    @Operation(summary = "获取调查表单导入模板")
    @Parameter(name = "surveyCode", description = "表单编码", required = false, in = ParameterIn.QUERY)
    public void importTemplate(@RequestParam(value = "surveyCode", required = false, defaultValue = "customer-satisfaction") String surveyCode, HttpServletResponse response) {
        //暂时只有客户满意度调查表单 code=customer-satisfaction
        String defaultCode = "customer-satisfaction";
        if (surveyCode.equals(defaultCode)) {
            List<SurveyRecordExcelReq> list = Arrays.asList(SurveyRecordExcelReq.builder()
                    .projectName("e能管家客户服务小程序").projectNum("PRJ-2024-001").projectType("XX项目").serviceUnit("科技公司")
                    .serviceAddr("平云路163号").returnVisitor("e能管家").returnPhone("13688888888").clientName("南投集团")
                    .clientPhone("13866666666").remark("示例记录，导入时请删除！").build()
            );
            ExcelUtils.write(response, "调查表单导入模板.xls", "调查表单列表", SurveyRecordExcelReq.class, list);
        } else {
            throw new ServiceException("表单编码错误，操作失败！");
        }
    }

    @ApiOperationSupport(order = 8)
    @PostMapping("/record/import")
    @Operation(summary = "导入调查表单")
    @Parameter(name = "surveyCode", description = "表单编码", required = false)
    @SneakyThrows
    public CommonResult<Boolean> importExcel(@RequestPart("file") MultipartFile file,
                                             @RequestParam(value = "surveyCode", required = false, defaultValue = "customer-satisfaction") String surveyCode) {
        //暂时只有客户满意度调查表单 code=customer-satisfaction
        List<SurveyRecordExcelReq> list = ExcelUtils.read(file, SurveyRecordExcelReq.class);
        return CommonResult.suc(surveyRecordService.importExcel(list, surveyCode));
    }

    @ApiOperationSupport(order = 9)
    @PostMapping("/record/export")
    @Operation(summary = "调查表单导出")
    public void export(@RequestBody SurveyRecordReq req, HttpServletResponse response) {
        surveyRecordService.exportExcel(req, response);
    }

}

