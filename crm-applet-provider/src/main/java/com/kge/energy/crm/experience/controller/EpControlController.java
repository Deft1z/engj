package com.kge.energy.crm.experience.controller;

import cn.hutool.core.util.ObjectUtil;
import com.kge.energy.crm.common.util.RedisUtils;
import com.kge.energy.crm.experience.resp.EpControlEnabledResp;
import com.kge.platform.framework.common.net.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 小程序体验租户控制接口
 *
 * @author tangchenghui
 */
@RestController
@RequestMapping("/experience/control")
@RequiredArgsConstructor
public class EpControlController {

    private final RedisUtils redisUtils;

    @Operation(summary = "获取是否允许处理")
    @GetMapping("/enabled")
    public CommonResult<EpControlEnabledResp> enabled() {

        String enableSubmitWokrOrder = redisUtils.get("experience:control:submit_wokr_order");

        EpControlEnabledResp resp = new EpControlEnabledResp()
                .setSubmitWokrOrder(ObjectUtil.equals(enableSubmitWokrOrder, "true"));

        return CommonResult.suc(resp);
    }

}
