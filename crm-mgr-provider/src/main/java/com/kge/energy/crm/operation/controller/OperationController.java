package com.kge.energy.crm.operation.controller;

import cn.hutool.core.bean.BeanUtil;
import com.kge.energy.crm.common.go.ConvertToGoFormats;
import com.kge.energy.crm.common.net.CommonResponse;
import com.kge.energy.crm.external.ecc.service.EccService;
import com.kge.energy.crm.operation.req.OperationDetailReq;
import com.kge.energy.crm.operation.req.OperationListReq;
import com.kge.energy.crm.operation.resp.OperationDetailResp;
import com.kge.energy.crm.operation.service.OperationService;
import com.kge.energy.crm.repository.entityext.param.OperationParam;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;

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
    public CommonResponse<Object> getPage(@RequestBody OperationListReq req) throws NoSuchAlgorithmException {
        return CommonResponse.suc(operationService.getPage(req));
    }

    /**
     * 查询运维记录详情
     */
    // TODO 后续可能会取消这个接口
    @ConvertToGoFormats
    @PostMapping("/omBack/report/back")
    public CommonResponse<OperationDetailResp> getDetail(@RequestBody OperationDetailReq req) {
        OperationParam param = BeanUtil.copyProperties(req, OperationParam.class);
        return CommonResponse.suc(
                Optional.ofNullable(operationService.getDetail(param))
                        .map(res -> BeanUtil.copyProperties(res, OperationDetailResp.class))
                        .orElse(new OperationDetailResp())
        );
    }

    /**
     * 查看附件
     */
    // TODO 当前url地址为临时命名，待前端修改时再确定url地址命名
    @GetMapping("/omBack/file/test/{*filePath}")
    public Resource getFile(@PathVariable("filePath") String filePath) {
        return eccService.getFile(filePath);
    }
}
