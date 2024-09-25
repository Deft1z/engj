package com.kge.energy.crm.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kge.energy.crm.repository.entity.CfBizFunctionMsg;
import com.kge.energy.crm.repository.entityext.result.CfBizFunctionMsgResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 业务功能消息配置表(CfBizFunctionMsgConfig)数据库访问层
 *
 * @author zhengwenke
 * @since 2024-09-19 10:30:28
 */
public interface CfBizFunctionMsgMapper extends BaseMapper<CfBizFunctionMsg> {

    List<CfBizFunctionMsgResult> getFunctionConfigs(@Param("bizFunctionId") Integer bizFunctionId, @Param("tenantId") Integer tenantId);

}

