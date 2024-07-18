package com.kge.energy.crm.content.controller;

import cn.hutool.core.bean.BeanUtil;
import com.kge.energy.crm.common.execption.BadException;
import com.kge.energy.crm.common.net.CommonResponse;
import com.kge.energy.crm.common.net.ResponseCode;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.content.req.ModuleAddReq;
import com.kge.energy.crm.content.req.ModuleDeleteReq;
import com.kge.energy.crm.content.req.ModuleEditReq;
import com.kge.energy.crm.content.req.ModuleReq;
import com.kge.energy.crm.content.service.ModuleService;
import com.kge.energy.crm.repository.entity.CmsBlock;
import com.kge.energy.crm.repository.entityext.param.ModuleParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/contentMgrBack/moduleMgr")
@RequiredArgsConstructor
public class ModuleController {
    private final ModuleService moduleService;

    /**
     * 分页查询模块记录
     */
    @PostMapping("/findBackPage")
    public CommonResponse<PageResp<CmsBlock>> getPage(@Validated @RequestBody ModuleReq req) {
        ModuleParam param = BeanUtil.copyProperties(req, ModuleParam.class);
        param.setName(Optional.ofNullable(req.getSearchMap()).map(e -> e.get("name")).orElse(null));

        PageResp<CmsBlock> resp = new PageResp<>(moduleService.getPage(param));
        return CommonResponse.suc(resp);
    }

    /**
     * 添加模块
     */
    @PostMapping("/opt/add")
    public CommonResponse<Boolean> add(@Validated @RequestBody ModuleAddReq req) {
        CmsBlock cmsBlock = BeanUtil.copyProperties(req, CmsBlock.class);
        return CommonResponse.suc(moduleService.add(cmsBlock));
    }


    /**
     * 修改模块信息
     */
    @PostMapping("/opt/update")
    public CommonResponse<Boolean> edit(@Validated @RequestBody ModuleEditReq req) {
        if (req.getFlag() != null && req.getFlag() != 1 && req.getFlag() != -2) {
            throw new BadException(ResponseCode.PARAM_NOT_VALID);
        }

        CmsBlock cmsBlock = BeanUtil.copyProperties(req, CmsBlock.class);
        return CommonResponse.suc(moduleService.edit(cmsBlock));
    }

    /**
     * 删除模块
     */
    @PostMapping("/opt/del")
    public CommonResponse<Boolean> delete(@Validated @RequestBody ModuleDeleteReq req) {
        return CommonResponse.suc(moduleService.delete(req.getIds()));
    }
}
