package com.kge.energy.crm.content.controller;

import cn.hutool.core.bean.BeanUtil;
import com.kge.energy.crm.common.net.CommonResponse;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.content.req.*;
import com.kge.energy.crm.content.service.ModuleContentService;
import com.kge.energy.crm.repository.entity.CmsBlockContent;
import com.kge.energy.crm.repository.entityext.param.ModuleContentParam;
import com.kge.energy.crm.repository.entityext.result.CmsBlockContentType;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/contentMgrBack/moduleContentMgr")
@RequiredArgsConstructor
public class ModuleContentController {
    private final ModuleContentService moduleContentService;

    /**
     * 分页查询模块内容记录
     */
    @PostMapping("/findPage")
    public CommonResponse<PageResp<CmsBlockContentType>> getPage(@Validated @RequestBody ModuleContentReq req) {
        ModuleContentParam param = BeanUtil.copyProperties(req, ModuleContentParam.class);
        param.setName(Optional.ofNullable(req.getSearchMap()).map(e -> e.get("name")).orElse(null));

        PageResp<CmsBlockContentType> resp = new PageResp<>(moduleContentService.getPage(param));
        return CommonResponse.suc(resp);
    }

    /**
     * 添加模块内容
     */
    @PostMapping("/opt/add")
    public CommonResponse<Boolean> add(@Validated @RequestBody ModuleContentAddReq req) {
        CmsBlockContent content = BeanUtil.copyProperties(req, CmsBlockContent.class);
        return CommonResponse.suc(moduleContentService.add(content));
    }

    /**
     * 修改模块内容信息
     */
    @PostMapping("/opt/update")
    public CommonResponse<Boolean> edit(@Validated @RequestBody ModuleContentEditReq req) {
        CmsBlockContent content = BeanUtil.copyProperties(req, CmsBlockContent.class);
        return CommonResponse.suc(moduleContentService.edit(content));
    }

    /**
     * 删除模块内容
     */
    @PostMapping("/opt/del")
    public CommonResponse<Boolean> delete(@Validated @RequestBody ModuleContentDeleteReq req) {
        return CommonResponse.suc(moduleContentService.delete(req.getIds()));
    }
}
