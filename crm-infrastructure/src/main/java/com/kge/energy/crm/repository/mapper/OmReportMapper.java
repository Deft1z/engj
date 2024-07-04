package com.kge.energy.crm.repository.mapper;

import com.kge.energy.crm.repository.entity.OmReport;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kge.energy.crm.repository.entityext.param.OmReportListParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * om_report(OmReport)表数据库接口层
 */
public interface OmReportMapper extends BaseMapper<OmReport> {

    List<OmReport> getList(OmReportListParam param);
}

