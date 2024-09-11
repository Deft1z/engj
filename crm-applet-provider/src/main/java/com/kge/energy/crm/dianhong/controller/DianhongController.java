package com.kge.energy.crm.dianhong.controller;

import com.kge.energy.crm.dianhong.resp.DhStatisticResp;
import com.kge.energy.crm.dianhong.service.DianhongService;
import com.kge.platform.framework.common.net.CommonResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping()
public class DianhongController {

    private final DianhongService dianhongService;

    @GetMapping("/dianhong/getDianhongStatistic")
    public CommonResult<DhStatisticResp> selectPcsDeviceList() {
        return CommonResult.suc(dianhongService.getDianhongStatistic());
    }

}
