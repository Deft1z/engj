package com.kge.energy.crm.company.controller;

import cn.hutool.core.bean.BeanUtil;
import com.kge.energy.crm.common.go.ConvertToGoFormats;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.company.req.CompanyEditReq;
import com.kge.energy.crm.company.req.CompanyReq;
import com.kge.energy.crm.company.resp.CompanyResp;
import com.kge.energy.crm.company.service.CompanyService;
import com.kge.energy.crm.repository.entityext.param.CompanyParam;
import com.kge.platform.framework.common.net.CommonResult;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/baseDataBack/organizationBackMrg")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;

    /**
     * 分页查询公司列表
     */
    @ConvertToGoFormats
    @PostMapping("/companyList/load")
    public CommonResult<PageResp<CompanyResp>> getPage(@Validated @RequestBody CompanyReq req) {
        CompanyParam param = BeanUtil.copyProperties(req, CompanyParam.class);
        PageResp<CompanyResp> resp = new PageResp<>(companyService.getPage(param));
        return CommonResult.suc(resp);
    }

    /**
     * 修改公司信息
     */
    @PostMapping("/companyForm/update")
    public CommonResult<Boolean> edit(@Validated @RequestBody CompanyEditReq req) {
        CompanyParam param = BeanUtil.copyProperties(req, CompanyParam.class);
        return CommonResult.suc(companyService.edit(param));
    }

    /**
     * 修改公司图片
     */
    // TODO 建议后续合并到修改公司信息接口
    @PostMapping("/companyList/update")
    public CommonResult<Boolean> editCover(@Validated @RequestBody CompanyEditReq req) {
        CompanyParam param = BeanUtil.copyProperties(req, CompanyParam.class);
        return CommonResult.suc(companyService.editCover(param));
    }
}
