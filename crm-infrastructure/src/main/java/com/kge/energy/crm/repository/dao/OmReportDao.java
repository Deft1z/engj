package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.repository.entity.OmReport;
import com.kge.energy.crm.repository.entityext.param.OperationParam;
import com.kge.energy.crm.repository.entityext.result.OperationDetail;
import com.kge.energy.crm.repository.mapper.OmReportMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * om_report(OmReport)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class OmReportDao extends ServiceImpl<OmReportMapper, OmReport> {

    private final OmReportMapper mapper;

    public OperationDetail getDetail(OperationParam param) {
        return mapper.getDetail(param);
    }
}

