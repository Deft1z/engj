package com.kge.energy.crm.experience.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.kge.energy.crm.application.req.AppDetailReq;
import com.kge.energy.crm.application.resp.AppDetailResp;
import com.kge.energy.crm.application.service.ApplicationService;
import com.kge.energy.crm.experience.resp.EpAppDetailResp;
import com.kge.platform.framework.common.net.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 小程序用户家园接口
 *
 * @author tangchenghui
 */
@RestController
@RequestMapping("/experience")
@RequiredArgsConstructor
public class EpApplicationController {

    private final ApplicationService applicationService;

    @Operation(summary = "获取APP详情信息")
    @PostMapping("/application/detail")
    public CommonResult<List<EpAppDetailResp>> getAppDetail(@Validated @RequestBody AppDetailReq appTokenReq) {

        List<AppDetailResp> resps = applicationService.getAppDetail(appTokenReq);

        List<EpAppDetailResp> epResps = BeanUtil.copyToList(resps, EpAppDetailResp.class);

        for (EpAppDetailResp epResp : epResps) {
            if (ObjectUtil.equals(epResp.getAppId(), 1)) {
                // 光伏应用
                EpAppDetailResp.ExperienceAttachment attachment = new EpAppDetailResp.ExperienceAttachment()
                        .setType("总览")
                        .setFilepath("1809a4b784788ff68bd342d546000002");
                epResp.setExperienceAttachments(List.of(attachment));

            } else if (ObjectUtil.equals(epResp.getAppId(), 2)) {
                // 电房应用
                EpAppDetailResp.ExperienceAttachment attachment = new EpAppDetailResp.ExperienceAttachment()
                        .setType("总览")
                        .setFilepath("1809a4cb83725dc48bd342d546000003");
                epResp.setExperienceAttachments(List.of(attachment));
            }
        }

        return CommonResult.suc(epResps);
    }

}
