package com.kge.energy.crm.msg.controller;

import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.msg.req.UserMsgListReq;
import com.kge.energy.crm.msg.service.UserMsgService;
import com.kge.energy.crm.repository.entityext.result.UserMsgListResult;
import com.kge.platform.framework.common.net.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "用户消息")
@RestController
@RequestMapping("/userMsg")
@RequiredArgsConstructor
public class UserMsgController {

    private final UserMsgService userMsgService;

    @Operation(summary = "消息列表")
    @PostMapping("/list")
    public CommonResult<PageResp<UserMsgListResult>> list(@Validated @RequestBody UserMsgListReq req) {
        return CommonResult.suc(userMsgService.list(req));
    }

    @Operation(summary = "消息列表导出")
    @PostMapping(value = "/list/export")
    public void export(@RequestBody UserMsgListReq req, HttpServletResponse response) {
        userMsgService.exportList(response, req);
    }


}