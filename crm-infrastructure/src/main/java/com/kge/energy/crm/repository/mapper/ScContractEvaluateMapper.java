package com.kge.energy.crm.repository.mapper;

import com.kge.energy.crm.repository.entity.ScContractEvaluate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kge.energy.crm.repository.entityext.result.StartEva;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * sc_contract_evaluate 服务合同评价(ScContractEvaluate)表数据库接口层
 */
public interface ScContractEvaluateMapper extends BaseMapper<ScContractEvaluate> {

    List<StartEva> getStartEva(@Param("star") Integer star);

    Float getAverage();
}

