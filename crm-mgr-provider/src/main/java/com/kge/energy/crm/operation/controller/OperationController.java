package com.kge.energy.crm.operation.controller;

import cn.hutool.core.bean.BeanUtil;
import com.kge.energy.crm.common.go.ConvertToGoFormats;
import com.kge.energy.crm.external.ecc.service.EccService;
import com.kge.energy.crm.operation.req.OperationDetailReq;
import com.kge.energy.crm.operation.req.OperationListReq;
import com.kge.energy.crm.operation.resp.OperationDetailResp;
import com.kge.energy.crm.operation.service.OperationService;
import com.kge.energy.crm.repository.entityext.param.OperationParam;
import com.kge.platform.framework.common.net.CommonResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

/**
 * 运维列表接口
 *
 * @author wangjihua
 */
@RestController
@RequiredArgsConstructor
public class OperationController {

    private final OperationService operationService;

    private final EccService eccService;

    /**
     * 查询运维记录列表
     */
    @ConvertToGoFormats
    @PostMapping("/external/getRecord")
    public CommonResult<Object> getPage(@RequestBody OperationListReq req) throws NoSuchAlgorithmException {
        return CommonResult.suc(operationService.getPage(req));
    }

    /**
     * 查询运维记录详情
     */
    // TODO 后续可能会取消这个接口
    @ConvertToGoFormats
    @PostMapping("/omBack/report/back")
    public CommonResult<OperationDetailResp> getDetail(@RequestBody OperationDetailReq req) {
        OperationParam param = BeanUtil.copyProperties(req, OperationParam.class);
        return CommonResult.suc(
                Optional.ofNullable(operationService.getDetail(param))
                        .map(res -> BeanUtil.copyProperties(res, OperationDetailResp.class))
                        .orElse(new OperationDetailResp())
        );
    }

}
