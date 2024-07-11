package com.kge.energy.crm.opetation.controller;

import cn.hutool.core.bean.BeanUtil;
import com.kge.energy.crm.common.net.CommonResponse;
import com.kge.energy.crm.external.ecc.req.Condition;
import com.kge.energy.crm.external.ecc.req.EccReq;
import com.kge.energy.crm.external.ecc.resp.EccMaintenance;
import com.kge.energy.crm.external.ecc.resp.EccPageData;
import com.kge.energy.crm.external.ecc.resp.EccResp;
import com.kge.energy.crm.external.ecc.service.EccService;
import com.kge.energy.crm.opetation.req.OperationDetailReq;
import com.kge.energy.crm.opetation.req.OperationListReq;
import com.kge.energy.crm.opetation.resp.OperationDetailResp;
import com.kge.energy.crm.opetation.service.OperationService;
import com.kge.energy.crm.repository.entityext.param.OperationParam;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.stream.Collectors;

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
    @PostMapping("/external/getRecord")
    public CommonResponse<Object> getList(@RequestBody OperationListReq req) throws NoSuchAlgorithmException {

        // 构造ecc接口请求参数
        EccReq eccReq = new EccReq();
        eccReq.setPageNo(req.getPageNo());
        eccReq.setPageSize(req.getPageSize());
        // 构造查询条件
        Condition condition = new Condition();
        condition.setRiskRates(new String[]{"设备巡检", "设备试验", "设备维修", "设备检修、抢修作业"});
        if (!req.getPhone().trim().equals("")) {
            condition.setFirstPartyContactsPhone(req.getPhone());
        }
        eccReq.setCondition(condition);

        EccResp<EccPageData<EccMaintenance>> eccResp = eccService.getMaintenanceList(eccReq);

        // 转换attachment中的路径
        Optional.ofNullable(eccResp)
                .map(EccResp::getData)
                .map(EccPageData::getList)
                .map(list -> {
                    list.forEach(e ->
                            e.setAttactments(
                                    e.getAttactments().stream()
                                            .map(attachment -> attachment.setUrl(attachment.getUrl().replace("https://ecc.nftz:8181", "")))
                                            .collect(Collectors.toList())
                            )
                    );
                    return list;
                });

        return CommonResponse.suc(eccResp);
    }

    /**
     * 查询运维记录详情
     */
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
    @GetMapping("/omBack/file/test/{*filePath}")
    public Resource getFile(@PathVariable("filePath") String filePath) {
        return eccService.getFile(filePath);
    }
}
