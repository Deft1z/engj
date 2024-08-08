package com.kge.energy.crm.pv.controller;

import com.kge.energy.crm.common.go.ConvertToGoFormats;
import com.kge.energy.crm.common.net.CommonResponse;
import com.kge.energy.crm.external.epcpv.req.EpcpvDetailsReq;
import com.kge.energy.crm.organization.req.OrgReq;
import com.kge.energy.crm.pv.req.PvCommentReq;
import com.kge.energy.crm.pv.req.PvDetailReq;
import com.kge.energy.crm.pv.req.PvInfoReq;
import com.kge.energy.crm.pv.req.PvLikeReq;
import com.kge.energy.crm.pv.service.PvService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class PvController {

    private final PvService pvService;

    /**
     * @description 小程序端我的 - 获取光伏项目管理数据接口
     * @author tangchenghui
     * @date 2024/7/31 17:28
    */
    @ConvertToGoFormats
    @GetMapping("/Management/Pv/all")
    public CommonResponse<Object> getAllPvInfo(@RequestBody PvInfoReq pvInfoReq){
        return CommonResponse.suc(pvService.getAllPvInfo(pvInfoReq));
    }

    /**
     * @description 小程序端我的 - 光伏项目评论接口
     * @author tangchenghui
     * @date 2024/8/1 10:40
    */
    @ConvertToGoFormats
    @PostMapping("/Management/Pv/commnet/add")
    public CommonResponse<Object> commentPv(@Validated @RequestBody PvCommentReq pvCommentReq){
        return CommonResponse.suc(pvService.commentPv(pvCommentReq));
    }

    /**
     * @description 小程序端我的 - 光伏项目点赞评论接口
     * @author tangchenghui
     * @date 2024/8/1 10:40
     */
    @ConvertToGoFormats
    @PostMapping("/Management/Pv/like/add")
    public CommonResponse<Object> commentPv(@Validated @RequestBody PvLikeReq pvLikeReq){
        return CommonResponse.suc(pvService.likeComment(pvLikeReq));
    }

    @ConvertToGoFormats
    @GetMapping("/Management/Pv/detail")
    public CommonResponse<Object> commentPv(@Validated @RequestBody PvDetailReq req){
        return CommonResponse.suc(pvService.getProjectDetailsList(req));
    }
}
