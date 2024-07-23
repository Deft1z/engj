package com.kge.energy.crm.app.controller;

import com.kge.energy.crm.app.req.*;
import com.kge.energy.crm.app.resp.AppDetailUserResp;
import com.kge.energy.crm.app.resp.DetailResp;
import com.kge.energy.crm.app.resp.ListResp;
import com.kge.energy.crm.app.resp.UserResp;
import com.kge.energy.crm.app.service.AppService;
import com.kge.energy.crm.common.execption.BadException;
import com.kge.energy.crm.common.go.ConvertToGoFormats;
import com.kge.energy.crm.common.net.CommonResponse;
import com.kge.energy.crm.common.net.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/baseDataBack/internalBind")
@RequiredArgsConstructor
public class InternalBindController {

    private final AppService appService;

    /**
     * 绑定管理 -> 获取客户未绑定的应用列表 (由于前端需要分页，所以先将uid GROUPBY 分页查询，然后查出openIID_share表的数据，合并同一个user_id的内容，还要特殊处理APPid=2的情况)
     */
    @ConvertToGoFormats
    @GetMapping("/list")
    public CommonResponse<ListResp> list(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit,
                                         @RequestParam(name="appid",required = false) Integer appid,
                                         @RequestParam(name = "mobile",required = false) String mobile,
                                         @RequestParam(name = "name",required = false) String name) {
        return CommonResponse.suc(appService.listNew(page, limit, appid, mobile, name));
    }

    /**
     * 绑定管理 -> 详情
     */
    @ConvertToGoFormats
    @GetMapping("/detail")
    public CommonResponse<DetailResp> detail(@RequestParam("page") Integer page,
                                                   @RequestParam(name="limit",required = false) Integer limit,
                                                   @RequestParam(name = "ids",required = false) List<Integer> ids,
                                                   @RequestParam(name = "appid",required = false) Integer appid,
                                                   @RequestParam(name = "total",required = false) Integer total,
                                                   @RequestParam(name = "mobile",required = false) String mobile,
                                                   @RequestParam(name = "name",required = false) String name) {

        DetailResp result = appService.FindBindList(page, limit, mobile, name ,ids);
        return CommonResponse.suc(result);
    }

    /**
     * 绑定管理 -> 增加项目
     */
    @ConvertToGoFormats
    @PostMapping("/addProject/add")
    public CommonResponse<Integer> addProject(@RequestBody AddProReq req) {
        int resultId = appService.addProject(req);
        if ( resultId == 0 ) {
            throw new BadException(ResponseCode.DB_INSERT_FAIL);
        }else {
            return CommonResponse.suc(resultId);
        }
    }

    /**
     * 绑定管理 -> 取消项目
     */
    @ConvertToGoFormats
    @PostMapping("/project/update")
    public CommonResponse<Boolean> delProject(@RequestBody ProjectDelReq req) {
        int resultInt = appService.Del(req);
        if (resultInt == 0) {
            throw new BadException(ResponseCode.DB_UPDATE_FAIL);
        }
        return CommonResponse.suc(true);
    }

    /**
     * 绑定管理 -> 取消该用户已绑定应用的关联
     */
    @ConvertToGoFormats
    @PostMapping("/cancel/update")
    public CommonResponse<Boolean> cancel(@RequestBody BindReq req) {
        int resultInt = appService.CancelAndUpdate(req);
        if (resultInt == 0) {
            throw new BadException(ResponseCode.DB_UPDATE_FAIL);
        }
        return CommonResponse.suc(true);
    }

    /**
     * 绑定管理 -> 取消所有用户关联的应用记录
     */
    @ConvertToGoFormats
    @PostMapping("/cancelall/update")
    public CommonResponse<Boolean> cancelAll(@RequestBody CancelAllReq req) {
        int resultInt = appService.CancelAll(req.getOpenid());
        if (resultInt == 0) {
            throw new BadException(ResponseCode.DB_UPDATE_FAIL);
        }
        return CommonResponse.suc(true);
    }

    /**
     * 绑定管理 -> 查找改手机号下绑定的关联系统
     */
    @ConvertToGoFormats
    @GetMapping("/finduser")
    public CommonResponse<UserResp> findUserResp(@RequestParam("mobile") String mobile) {
        return CommonResponse.suc(appService.FindUserResp(mobile));
    }

    /**
     * 绑定管理 -> 查找改手机号下绑定的关联系统
     */
    @ConvertToGoFormats
    @PostMapping("/bind/add")
    public CommonResponse<Boolean> bindApp(@RequestBody BindReq req) {
        return CommonResponse.suc(appService.bindApp(req));
    }
}
