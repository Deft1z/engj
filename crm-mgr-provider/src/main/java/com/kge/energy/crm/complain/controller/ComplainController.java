package com.kge.energy.crm.complain.controller;

import com.kge.energy.crm.common.go.ConvertToGoFormats;
import com.kge.energy.crm.common.net.CommonResponse;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.complain.req.ComplainListReq;
import com.kge.energy.crm.complain.req.ComplainReplyReq;
import com.kge.energy.crm.complain.resp.ComplainListResp;
import com.kge.energy.crm.complain.service.ComplainService;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    public CommonResponse<PageResp<ComplainListResp>> getComplainList(@RequestBody ComplainListReq complainListReq){
        return CommonResponse.suc(complainService.getComplainList(complainListReq));
    }

    @ConvertToGoFormats
    @PostMapping("/feedback/insert")
    public CommonResponse<Boolean> replyComplain(@Validated @RequestBody ComplainReplyReq complainReplyReq){
        return CommonResponse.suc(complainService.replyComplain(complainReplyReq));
    }

}
