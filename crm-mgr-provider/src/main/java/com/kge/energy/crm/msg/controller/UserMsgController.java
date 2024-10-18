package com.kge.energy.crm.msg.controller;

import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.msg.req.UserMsgListReq;
import com.kge.energy.crm.msg.service.UserMsgService;
import com.kge.energy.crm.repository.entityext.result.UserMsgListResult;
import com.kge.platform.framework.common.net.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "获取个人未读消息总数")
    @Parameter(name = "msgBizType", description = "消息业务类型， 0 告警信息 1 工单通知 2 项目合同 3 投诉处理", required = false, in = ParameterIn.QUERY)
    @GetMapping("/getUnreadCount")
    public CommonResult<Integer> getUnreadCount(@RequestParam(value = "msgBizType", required = false) Integer msgBizType) {
        return CommonResult.suc(userMsgService.getUnreadCount(msgBizType));
    }

    @Operation(summary = "根据id标记个人消息为已读")
    @Parameter(name = "id", description = "消息id", required = true, in = ParameterIn.QUERY)
    @GetMapping("/read")
    public CommonResult<Boolean> read(@RequestParam(value = "id") Integer id) {
        return CommonResult.suc(userMsgService.readById(id));
    }

    @Operation(summary = "一键标记全部个人消息为已读")
    @Parameter(name = "msgBizType", description = "消息业务类型， 0 告警信息 1 工单通知 2 项目合同 3 投诉处理", required = false, in = ParameterIn.QUERY)
    @GetMapping("/readAll")
    public CommonResult<Boolean> readAll(@RequestParam(value = "msgBizType", required = false) Integer msgBizType) {
        return CommonResult.suc(userMsgService.readByMsgBizType(msgBizType));
    }


}