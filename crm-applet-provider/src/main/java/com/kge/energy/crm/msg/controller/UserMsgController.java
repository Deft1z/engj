package com.kge.energy.crm.msg.controller;

import com.kge.energy.crm.common.net.CommonResponse;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.msg.req.UserMsgListReq;
import com.kge.energy.crm.repository.entityext.result.UserMsgListResult;
import com.kge.energy.crm.msg.service.UserMsgService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangjihua
 */
@Tag(name = "用户消息")
@RestController
@RequestMapping("/userMsg")
@RequiredArgsConstructor
public class UserMsgController {

    private final UserMsgService userMsgService;

    @Operation(summary = "当前登录用户消息列表")
    @GetMapping("/currentUserMsgList")
    public CommonResponse<PageResp<UserMsgListResult>> currentUserMsgList(@Validated @RequestBody UserMsgListReq req) {
        return CommonResponse.suc(userMsgService.getUserAlatmMsgList(req));
    }
}
