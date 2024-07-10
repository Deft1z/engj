package com.kge.energy.crm.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kge.energy.crm.repository.entity.OmReport;
import com.kge.energy.crm.repository.entityext.param.OperationParam;
import com.kge.energy.crm.repository.entityext.result.OperationDetail;

/**
 * om_report(OmReport)表数据库接口层
 */
public interface OmReportMapper extends BaseMapper<OmReport> {

    OperationDetail getDetail(OperationParam param);
}

