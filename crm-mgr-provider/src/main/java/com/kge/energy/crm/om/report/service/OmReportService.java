package com.kge.energy.crm.om.report.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.crypto.digest.MD5;
import com.kge.energy.crm.common.execption.BadException;
import com.kge.energy.crm.common.net.ResponseCode;
import com.kge.energy.crm.om.report.req.OmReportListReq;
import com.kge.energy.crm.om.report.resp.OmReportListResp;
import com.kge.energy.crm.repository.dao.OmReportDao;
import com.kge.energy.crm.repository.entity.OmReport;
import com.kge.energy.crm.repository.entityext.param.OmReportListParam;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 运维报告服务层
 * @author wangjihua
 */
@Service
@RequiredArgsConstructor
public class OmReportService {

    private final OmReportDao omReportDao;

    /**
     * 方法注释
     */
    public List<OmReportListResp> list(OmReportListReq req) {

        OmReportListParam param = new OmReportListParam()
                .setOperator(req.getOperator());

        List<OmReport> list = omReportDao.getList(param);

        if(CollectionUtil.isEmpty(list)){
            return Collections.EMPTY_LIST;
        }

         return list.stream()
                .map(item -> new OmReportListResp()
                        .setFormId(item.getFormId())
                        .setOperator(item.getOperator())
                ).collect(Collectors.toList());
    }
}
