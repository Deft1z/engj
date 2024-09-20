package com.kge.energy.crm.msg.controller;

import com.kge.energy.crm.common.go.ConvertToGoFormats;
import com.kge.energy.crm.msg.req.BizFunctionMsgConfigAddReq;
import com.kge.energy.crm.msg.resp.SysMsgChannelResp;
import com.kge.energy.crm.msg.service.SysMsgChannelService;
import com.kge.platform.framework.common.net.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 消息渠道(SysMsgChannel)Controller层
 *
 * @author zhengwenke
 * @since 2024-09-19 09:59:07
 */
@RestController
@RequestMapping("/api/v1/sysMsgChannel")
@Tag(name = "消息渠道")
@RequiredArgsConstructor
public class SysMsgChannelController {

    private final SysMsgChannelService sysMsgChannelService;

    @Operation(summary = "获取所有消息渠道")
    @GetMapping(value = "/getAll")
    @ConvertToGoFormats
    public CommonResult<List<SysMsgChannelResp>> getAll() {
        return CommonResult.suc(sysMsgChannelService.getAll());
    }

    @Operation(summary = "关联消息渠道和业务功能")
    @PostMapping(value = "/relateBizFunction")
    @ConvertToGoFormats
    public CommonResult<Boolean> relateBizFunction(@RequestBody @Valid BizFunctionMsgConfigAddReq bizFunctionMsgConfig) {
        return CommonResult.suc(sysMsgChannelService.relateBizFunction(bizFunctionMsgConfig));
    }

}

