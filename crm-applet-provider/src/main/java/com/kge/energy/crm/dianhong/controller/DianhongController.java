package com.kge.energy.crm.dianhong.controller;

import com.kge.energy.crm.common.net.CommonResponse;
import com.kge.energy.crm.dianhong.service.DianhongService;
import com.kge.energy.dh.resp.PcsDeviceResp;
import com.kge.energy.dh.service.SuiliangPvService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping()
public class DianhongController {

    private final DianhongService dianhongService;

    @GetMapping("/dianhong/getDianhongStatistic")
    public CommonResponse<Map<String, Object>> selectPcsDeviceList() {
        return CommonResponse.suc(dianhongService.getDianhongStatistic());
    }

}
