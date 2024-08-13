package com.kge.energy.crm.resource.controller;

import com.kge.energy.crm.common.go.ConvertToGoFormats;
import com.kge.energy.crm.common.net.CommonResponse;
import com.kge.energy.crm.resource.req.ResourceReq;
import com.kge.energy.crm.resource.resp.MenuNodeResp;
import com.kge.energy.crm.resource.service.BResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/baseDataBack/menu")
@RequiredArgsConstructor
@Deprecated //前端接新的菜单接口后删除
public class BResourceController {
    private final BResourceService bResourceService;

    /**
     * 获取用户有权限的接口列表
     */
    @ConvertToGoFormats
    @PostMapping("/list")
    @Deprecated //前端接新的菜单接口后删除
    public CommonResponse<List<MenuNodeResp>> menuList(@RequestBody ResourceReq req) {
        List<MenuNodeResp> menuArr = bResourceService.findMenu(req.getUserId());
        return CommonResponse.suc(menuArr);
    }
}
