package com.kge.energy.crm.event.controller;

import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.event.req.ReportEventReq;
import com.kge.energy.crm.event.service.EventTrackingDomainService;
import com.kge.platform.framework.common.net.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangjihua
 */
@Tag(name = "数据埋点")
@RestController
@RequestMapping("/eventTracking")
@RequiredArgsConstructor
public class EventTrackingController {

    private final EventTrackingDomainService eventTrackingDomainService;

    @Operation(summary = "上报埋点事件数据")
    @PostMapping("/report")
    public CommonResult<Boolean> report(@Validated @RequestBody ReportEventReq req) {
        eventTrackingDomainService.report(UserInfoContextUtils.getCurrentUserInfo(), req);
        return CommonResult.suc(true);
    }
}
