package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.repository.entity.CfBizFunctionMsg;
import com.kge.energy.crm.repository.entityext.result.CfBizFunctionMsgResult;
import com.kge.energy.crm.repository.mapper.CfBizFunctionMsgMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 业务功能消息配置表(CfBizFunctionMsgConfig)数据库访问层
 *
 * @author zhengwenke
 * @since 2024-09-19 10:30:28
 */
@Repository
@RequiredArgsConstructor
public class CfBizFunctionMsgDao extends ServiceImpl<CfBizFunctionMsgMapper, CfBizFunctionMsg> {

    private final CfBizFunctionMsgMapper mapper;

    public List<CfBizFunctionMsgResult> getFunctionConfigs(Integer bizFunctionId, Integer tenantId) {
        return mapper.getFunctionConfigs(bizFunctionId, tenantId);
    }

}

