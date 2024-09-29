package com.kge.energy.crm.complain.controller;

import com.kge.energy.crm.common.go.ConvertToGoFormats;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.complain.req.ComplainListExportReq;
import com.kge.energy.crm.complain.req.ComplainListReq;
import com.kge.energy.crm.complain.req.ComplainReplyReq;
import com.kge.energy.crm.complain.resp.ComplainListResp;
import com.kge.energy.crm.complain.service.ComplainService;
import com.kge.platform.framework.common.net.CommonResult;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/workMgrBack/complainBack")
@RequiredArgsConstructor
public class ComplainController {

    private final ComplainService complainService;

    @ConvertToGoFormats
    @PostMapping("/complain")
    public CommonResult<PageResp<ComplainListResp>> getComplainList(@RequestBody ComplainListReq complainListReq) {
        return CommonResult.suc(complainService.getComplainList(complainListReq));
    }

    @ConvertToGoFormats
    @PostMapping("/feedback/insert")
    public CommonResult<Boolean> replyComplain(@Validated @RequestBody ComplainReplyReq complainReplyReq) {
        return CommonResult.suc(complainService.replyComplain(complainReplyReq));
    }

    @ConvertToGoFormats
    @PostMapping("/complain/export")
    public CommonResult<Boolean> exportComplainList(@RequestBody ComplainListExportReq complainListExportReq) {
        return CommonResult.suc(complainService.exportComplainList(complainListExportReq));
    }

}
