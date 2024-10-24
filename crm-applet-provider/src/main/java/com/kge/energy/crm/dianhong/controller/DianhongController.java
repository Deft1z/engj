package com.kge.energy.crm.dianhong.controller;

import com.kge.energy.crm.dianhong.resp.DhStatisticResp;
import com.kge.energy.crm.dianhong.service.DianhongService;
import com.kge.energy.dh.req.DeviceControlDataReq;
import com.kge.energy.dh.req.DeviceControlEnableReq;
import com.kge.energy.dh.req.DeviceControlReq;
import com.kge.energy.dh.req.DeviceEnableReq;
import com.kge.energy.dh.resp.DeviceControlDataResp;
import com.kge.energy.dh.resp.DeviceControlEnableResp;
import com.kge.energy.dh.resp.DeviceControlResp;
import com.kge.energy.dh.resp.DeviceEnableResp;
import com.kge.platform.framework.common.net.CommonResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping()
public class DianhongController {

    private final DianhongService dianhongService;

    @GetMapping("/dianhong/getDianhongStatistic")
    public CommonResult<DhStatisticResp> selectPcsDeviceList() {
        return CommonResult.suc(dianhongService.getDianhongStatistic());
    }

    @PostMapping("/dianhong/setControlEnable")
    public CommonResult<DeviceEnableResp> setControlEnable(@Valid @RequestBody DeviceEnableReq req){
        return CommonResult.suc(dianhongService.setControlEnable(req));
    }

    @PostMapping("/dianhong/setControlPercent")
    public CommonResult<DeviceControlResp> setControlPercent(@Valid @RequestBody DeviceControlReq req){
        return CommonResult.suc(dianhongService.setControlPercent(req));
    }

    @PostMapping("/dianhong/getDeviceControlData")
    public CommonResult<DeviceControlDataResp> getDeviceControlData(@Valid @RequestBody DeviceControlDataReq req){
        return CommonResult.suc(dianhongService.getDeviceControlData(req));
    }

    @PostMapping("/dianhong/getControlEnable")
    public CommonResult<DeviceControlEnableResp> getControlEnable(@Valid @RequestBody DeviceControlEnableReq req){
        return CommonResult.suc(dianhongService.getControlEnable(req));
    }

}
