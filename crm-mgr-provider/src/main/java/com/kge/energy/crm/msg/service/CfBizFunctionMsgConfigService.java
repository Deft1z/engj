package com.kge.energy.crm.msg.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.msg.req.BizFunctionMsgConfigAddReq;
import com.kge.energy.crm.repository.dao.CfBizFunctionMsgDao;
import com.kge.energy.crm.repository.entity.CfBizFunctionMsg;
import com.kge.energy.crm.repository.entityext.result.CfBizFunctionMsgResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 业务功能消息配置(CfBizFunctionMsgConfig)Service层
 *
 * @author zhengwenke
 * @since 2024-09-19 10:30:28
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CfBizFunctionMsgConfigService {

    private final CfBizFunctionMsgDao cfBizFunctionMsgDao;

    public List<CfBizFunctionMsgResult> getFunctionConfigs(Integer bizFunctionId, Integer tenantId) {
        return cfBizFunctionMsgDao.getFunctionConfigs(bizFunctionId, tenantId);
    }

    @Transactional
    public Boolean save(BizFunctionMsgConfigAddReq bizFunctionMsgConfig) {
        UserInfoDto operator = UserInfoContextUtils.getCurrentUserInfo();
        //执行全删全插
        //删除关联关系
        cfBizFunctionMsgDao.remove(new QueryWrapper<CfBizFunctionMsg>()
                .lambda()
                .eq(CfBizFunctionMsg::getBizFunctionId, bizFunctionMsgConfig.getBizFunctionId())
                .eq(CfBizFunctionMsg::getTenantId, operator.getTenantId())
        );
        //新增关联关系
        if (bizFunctionMsgConfig.getMsgConfigs() != null && !bizFunctionMsgConfig.getMsgConfigs().isEmpty()) {
            for (BizFunctionMsgConfigAddReq.MsgConfigAddReq msgConfig : bizFunctionMsgConfig.getMsgConfigs()) {
                cfBizFunctionMsgDao.save(new CfBizFunctionMsg()
                        .setBizFunctionId(bizFunctionMsgConfig.getBizFunctionId())
                        .setBlacklist(msgConfig.getBlacklist())
                        .setWhitelist(msgConfig.getWhitelist())
                        .setPriority(msgConfig.getPriority())
                        .setEnabled(msgConfig.getEnabled())
                        .setMsgChannelId(msgConfig.getMsgChannelId())
                        .setCreateUserId(operator.getUserId().intValue())
                        .setTenantId(operator.getTenantId())
                );
            }
        }
        return true;
    }

}

