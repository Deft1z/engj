package com.kge.energy.crm.pv.controller;

import com.kge.energy.crm.common.go.ConvertToGoFormats;
import com.kge.energy.crm.common.net.CommonResponse;
import com.kge.energy.crm.organization.req.OrgReq;
import com.kge.energy.crm.pv.req.PvInfoReq;
import com.kge.energy.crm.pv.service.PvService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class PvController {

    private final PvService pvService;

    /**
     * @description 小程序端我的 - 获取光伏项目管理数据接口
     * @author tangchenghui
     * @date 2024/7/31 17:28
    */
    @ConvertToGoFormats
    @GetMapping("/Management/Pv/all")
    public CommonResponse<Object> getAllPvInfo(@RequestBody PvInfoReq pvInfoReq){
        return CommonResponse.suc(pvService.getAllPvInfo(pvInfoReq));
    }
}
