package com.kge.energy.crm.pv.controller;

import com.kge.energy.crm.common.go.ConvertToGoFormats;
import com.kge.energy.crm.pv.req.*;
import com.kge.energy.crm.pv.service.PvService;
import com.kge.platform.framework.common.net.CommonResult;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @PostMapping("/Management/Pv/all")
    public CommonResult<Object> getAllPvInfo(@RequestBody PvInfoReq pvInfoReq) {
        return CommonResult.suc(pvService.getAllPvInfo(pvInfoReq));
    }

    /**
     * @description 小程序端我的 - 光伏项目评论接口
     * @author tangchenghui
     * @date 2024/8/1 10:40
     */
    @ConvertToGoFormats
    @PostMapping("/Management/Pv/commnet/add")
    public CommonResult<Object> commentPv(@Validated @RequestBody PvCommentReq pvCommentReq) {
        return CommonResult.suc(pvService.commentPv(pvCommentReq));
    }

    /**
     * @description 小程序端我的 - 光伏项目删除评论接口
     * @author tangchenghui
     * @date 2024/8/22 10:40
     */
    @ConvertToGoFormats
    @PostMapping("/Management/Pv/commnet/del")
    public CommonResult<Object> commentPvDel(@Validated @RequestBody PvCommentDelReq pvCommentDelReq) {
        return CommonResult.suc(pvService.commentPvDel(pvCommentDelReq));
    }

    /**
     * @description 小程序端我的 - 光伏项目点赞评论接口
     * @author tangchenghui
     * @date 2024/8/1 10:40
     */
    @ConvertToGoFormats
    @PostMapping("/Management/Pv/like/add")
    public CommonResult<Object> commentPv(@Validated @RequestBody PvLikeReq pvLikeReq) {
        return CommonResult.suc(pvService.likeComment(pvLikeReq));
    }

    @ConvertToGoFormats
    @PostMapping("/Management/Pv/detail")
    public CommonResult<Object> commentPv(@Validated @RequestBody PvDetailReq req) {
        return CommonResult.suc(pvService.getProjectDetailsList(req));
    }
}
