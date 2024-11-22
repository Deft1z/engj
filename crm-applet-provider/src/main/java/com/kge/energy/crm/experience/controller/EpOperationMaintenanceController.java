package com.kge.energy.crm.experience.controller;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.kge.energy.crm.common.go.ConvertToGoFormats;
import com.kge.energy.crm.external.ecc.req.EccOperationDetailReq;
import com.kge.energy.crm.external.ecc.req.EccReq;
import com.kge.energy.crm.external.ecc.resp.EccMaintenance;
import com.kge.energy.crm.external.ecc.resp.EccPageData;
import com.kge.energy.crm.external.ecc.resp.EccResp;
import com.kge.platform.framework.common.net.CommonResult;
import com.kge.platform.framework.web.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 小程序运维托管接口
 *
 * @author tangchenghui
 */
@RestController
@RequestMapping("/experience")
@RequiredArgsConstructor
public class EpOperationMaintenanceController {

    @Value("${spring.profiles.active}")
    private String env;

    @ConvertToGoFormats
    @PostMapping("/external/getRecord")
    public CommonResult<EccResp<EccPageData<EccMaintenance>>> getRecordList(@RequestBody EccReq eccReq) throws NoSuchAlgorithmException, IOException {

        ClassPathResource resource = new ClassPathResource("json/experience/EccRecordList.json");

        EccResp<EccPageData<EccMaintenance>> resp = null;
        // 暂时写死一页，后续多页要改查json文件做分页
        if (ObjectUtil.equals(eccReq.getPageNo(), 1)) {
            resp = JsonUtils.getSource().readValue(resource.getStream(), new TypeReference<EccResp<EccPageData<EccMaintenance>>>() {
            });

            List<EccMaintenance> dataList = resp.getData()
                    .getList()
                    .stream()
                    .filter(item -> Arrays.stream(eccReq.getCondition().getRiskRates())
                            .toList().contains(item.getRiskRate()))
                    .collect(Collectors.toList());
            resp.getData().setList(dataList);

            if (ObjectUtil.equals(env, "prod")) {
                resp.getData().getList()
                        .forEach(item -> item.getAttactments()
                                .forEach(att -> att.setUrl(att.getUrl().replace("/fsbt", "")))
                        );
            }
        }

        resp.getData()
                .setSize(resp.getData().getList().size())
                .setTotal(resp.getData().getList().size());

        return CommonResult.suc(resp);
    }

    @PostMapping("/external/getRecordDetail")
    public CommonResult<EccMaintenance> getRecordList(@RequestBody EccOperationDetailReq req) throws NoSuchAlgorithmException, IOException {

        ClassPathResource resource = new ClassPathResource("json/experience/EccRecordDetail.json");

        List<EccMaintenance> eccMaintenances = JsonUtils.getSource().readValue(resource.getStream(), new TypeReference<List<EccMaintenance>>() {
        });

        EccMaintenance eccMaintenance = eccMaintenances.stream()
                .filter(item -> ObjectUtil.equals(item.getPlanId(), req.getPlanId()))
                .findFirst()

                .orElse(null);

        if (ObjectUtil.isNotNull(eccMaintenance) && ObjectUtil.equals(env, "prod")) {
            eccMaintenance.getAttactments()
                    .forEach(att -> att.setUrl(att.getUrl().replace("/fsbt", ""))
                    );
        }

        return CommonResult.suc(eccMaintenance);
    }


}
