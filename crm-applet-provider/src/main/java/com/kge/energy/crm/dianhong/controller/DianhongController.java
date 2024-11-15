package com.kge.energy.crm.dianhong.controller;

import cn.hutool.core.bean.BeanUtil;
import com.kge.energy.crm.common.util.ExcelUtils;
import com.kge.energy.crm.dianhong.resp.DhStatisticResp;
import com.kge.energy.crm.dianhong.service.DianhongService;
import com.kge.energy.dh.req.DeviceControlDataReq;
import com.kge.energy.dh.req.DeviceControlEnableReq;
import com.kge.energy.dh.req.DeviceControlReq;
import com.kge.energy.dh.req.DeviceEnableReq;
import com.kge.energy.dh.resp.*;
import com.kge.platform.framework.common.net.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping()
public class DianhongController {

    private final DianhongService dianhongService;

    @GetMapping("/dianhong/getDianhongStatistic")
    public CommonResult<DhStatisticResp> selectPcsDeviceList() {
        return CommonResult.suc(dianhongService.getDianhongStatistic());
    }

    @Operation(summary = "电鸿数据导出")
    @Parameter(name = "exportType", description = "导出类型：0 excel 1 pdf", required = false, in = ParameterIn.QUERY)
    @GetMapping("/dianhong/exportDianhongStatistic")
    @SneakyThrows
    public void exportDianhongStatistic(HttpServletResponse response, @RequestParam(value = "exportType", required = false) Integer exportType) {
        DhStatisticResp dianhongStatistic = dianhongService.getDianhongStatistic();
        Map<String, Object> dataMap = BeanUtil.beanToMap(dianhongStatistic.getTotal());
        PcsStatusStatisticResp status = dianhongStatistic.getStatus();
        dataMap.put("normalCount", Optional.ofNullable(status.getNormal()).map(PcsStatusResp::getCount).orElse(""));
        dataMap.put("normalPer", Optional.ofNullable(status.getNormal()).map(PcsStatusResp::getPer).orElse(""));
        dataMap.put("warningCount", Optional.ofNullable(status.getWarning()).map(PcsStatusResp::getCount).orElse(""));
        dataMap.put("warningPer", Optional.ofNullable(status.getWarning()).map(PcsStatusResp::getPer).orElse(""));
        dataMap.put("fixingCount", Optional.ofNullable(status.getFixingCount()).map(PcsStatusResp::getCount).orElse(""));
        dataMap.put("fixingPer", Optional.ofNullable(status.getFixingCount()).map(PcsStatusResp::getPer).orElse(""));

        InputStream templateInputStream = ResourceUtils.getURL("classpath:template/电力鸿蒙.xls").openStream();
        ExcelUtils.writeWithTemplate(response, templateInputStream, "电力鸿蒙.xls", dataMap, dianhongStatistic.getDevices(), exportType);
    }

    @PostMapping("/dianhong/setControlEnable")
    public CommonResult<DeviceEnableResp> setControlEnable(@Valid @RequestBody DeviceEnableReq req) {
        return CommonResult.suc(dianhongService.setControlEnable(req));
    }

    @PostMapping("/dianhong/setControlPercent")
    public CommonResult<DeviceControlResp> setControlPercent(@Valid @RequestBody DeviceControlReq req) {
        return CommonResult.suc(dianhongService.setControlPercent(req));
    }

    @PostMapping("/dianhong/getDeviceControlData")
    public CommonResult<DeviceControlDataResp> getDeviceControlData(@Valid @RequestBody DeviceControlDataReq req) {
        return CommonResult.suc(dianhongService.getDeviceControlData(req));
    }

    @PostMapping("/dianhong/getControlEnable")
    public CommonResult<DeviceControlEnableResp> getControlEnable(@Valid @RequestBody DeviceControlEnableReq req) {
        return CommonResult.suc(dianhongService.getControlEnable(req));
    }

}
