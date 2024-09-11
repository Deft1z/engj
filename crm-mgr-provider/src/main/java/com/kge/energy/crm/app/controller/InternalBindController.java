package com.kge.energy.crm.app.controller;

import com.kge.energy.crm.app.req.AddProReq;
import com.kge.energy.crm.app.req.BindReq;
import com.kge.energy.crm.app.req.CancelAllReq;
import com.kge.energy.crm.app.req.ProjectDelReq;
import com.kge.energy.crm.app.resp.DetailResp;
import com.kge.energy.crm.app.resp.ListResp;
import com.kge.energy.crm.app.resp.UserResp;
import com.kge.energy.crm.app.service.AppService;
import com.kge.energy.crm.common.go.ConvertToGoFormats;
import com.kge.platform.framework.common.net.CommonResult;
import lombok.RequiredArgsConstructor;
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
    public CommonResult<ListResp> list(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit,
                                       @RequestParam(name = "appid", required = false) Integer appid,
                                       @RequestParam(name = "mobile", required = false) String mobile,
                                       @RequestParam(name = "name", required = false) String name) {
        return CommonResult.suc(appService.listNew(page, limit, appid, mobile, name));
    }

    /**
     * 绑定管理 -> 详情
     */
    @ConvertToGoFormats
    @GetMapping("/detail")
    public CommonResult<DetailResp> detail(@RequestParam("page") Integer page,
                                           @RequestParam(name = "limit", required = false) Integer limit,
                                           @RequestParam(name = "ids", required = false) List<Integer> ids,
                                           @RequestParam(name = "appid", required = false) Integer appid,
                                           @RequestParam(name = "total", required = false) Integer total,
                                           @RequestParam(name = "mobile", required = false) String mobile,
                                           @RequestParam(name = "name", required = false) String name) {

        DetailResp result = appService.FindBindList(page, limit, mobile, name, ids);
        return CommonResult.suc(result);
    }

    /**
     * 绑定管理 -> 增加项目
     */
    @ConvertToGoFormats
    @PostMapping("/addProject/add")
    public CommonResult<Integer> addProject(@RequestBody AddProReq req) {
        int resultId = appService.addProject(req);
        return CommonResult.suc(resultId);
    }

    /**
     * 绑定管理 -> 取消项目
     */
    @ConvertToGoFormats
    @PostMapping("/project/update")
    public CommonResult<Boolean> delProject(@RequestBody ProjectDelReq req) {
        appService.Del(req);
        return CommonResult.suc(true);
    }

    /**
     * 绑定管理 -> 取消该用户已绑定应用的关联
     */
    @ConvertToGoFormats
    @PostMapping("/cancel/update")
    public CommonResult<Boolean> cancel(@RequestBody BindReq req) {
        appService.CancelAndUpdate(req);
        return CommonResult.suc(true);
    }

    /**
     * 绑定管理 -> 取消所有用户关联的应用记录
     */
    @ConvertToGoFormats
    @PostMapping("/cancelall/update")
    public CommonResult<Boolean> cancelAll(@RequestBody CancelAllReq req) {
        appService.CancelAll(req.getOpenid());
        return CommonResult.suc(true);
    }

    /**
     * 绑定管理 -> 查找改手机号下绑定的关联系统
     */
    @ConvertToGoFormats
    @GetMapping("/finduser")
    public CommonResult<UserResp> findUserResp(@RequestParam("mobile") String mobile) {
        return CommonResult.suc(appService.FindUserResp(mobile));
    }

    /**
     * 绑定管理 -> 查找改手机号下绑定的关联系统
     */
    @ConvertToGoFormats
    @PostMapping("/bind/add")
    public CommonResult<Boolean> bindApp(@RequestBody BindReq req) {
        return CommonResult.suc(appService.bindApp(req));
    }
}
