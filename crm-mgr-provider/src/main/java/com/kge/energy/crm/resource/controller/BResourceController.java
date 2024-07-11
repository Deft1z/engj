package com.kge.energy.crm.resource.controller;

import com.kge.energy.crm.common.net.CommonResponse;
import com.kge.energy.crm.repository.entityext.result.ResourcePermissionResult;
import com.kge.energy.crm.resource.req.ResourceReq;
import com.kge.energy.crm.resource.resp.MenuNodeResp;
import com.kge.energy.crm.resource.service.BResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/baseDataBack/menu")
@RequiredArgsConstructor
public class BResourceController {
    private final BResourceService bResourceService;

    /**
     * 获取用户有权限的接口列表
     */
    @PostMapping("/list")
    public CommonResponse<List<MenuNodeResp>> menuList(@RequestBody ResourceReq req) {
        List<MenuNodeResp> menuArr = bResourceService.findMenu(req.getUserId());
        return CommonResponse.suc(menuArr);
    }
}
