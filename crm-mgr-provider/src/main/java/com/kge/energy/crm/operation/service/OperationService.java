package com.kge.energy.crm.operation.service;

import com.kge.energy.crm.external.ecc.req.Condition;
import com.kge.energy.crm.external.ecc.req.EccReq;
import com.kge.energy.crm.external.ecc.resp.EccMaintenance;
import com.kge.energy.crm.external.ecc.resp.EccPageData;
import com.kge.energy.crm.external.ecc.resp.EccResp;
import com.kge.energy.crm.external.ecc.service.EccService;
import com.kge.energy.crm.operation.req.OperationListReq;
import com.kge.energy.crm.repository.dao.OmReportDao;
import com.kge.energy.crm.repository.entityext.param.OperationParam;
import com.kge.energy.crm.repository.entityext.result.OperationDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 */
@Service
@RequiredArgsConstructor
public class OperationService {

    private final EccService eccService;
    private final OmReportDao dao;

    public EccResp<EccPageData<EccMaintenance>> getPage(OperationListReq req) throws NoSuchAlgorithmException {
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
                                            .map(attachment -> attachment.setUrl(attachment.getUrl().replace(eccService.ECC_PREFIX, "")))
                                            .collect(Collectors.toList())
                            )
                    );
                    return list;
                });

        return eccResp;
    }

    public OperationDetail getDetail(OperationParam param) {
        return dao.getDetail(param);
    }
}
