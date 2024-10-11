package com.kge.energy.crm.msg.service;

import cn.hutool.core.bean.BeanUtil;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.msg.req.BizFunctionMsgConfigAddReq;
import com.kge.energy.crm.msg.resp.FunctionMsgChannelConfigResp;
import com.kge.energy.crm.repository.entityext.result.CfBizFunctionMsgResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 消息渠道(SysMsgChannel)Service层
 *
 * @author zhengwenke
 * @since 2024-09-19 09:59:04
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SysMsgChannelService {

    private final CfBizFunctionMsgConfigService bizFunctionMsgConfigService;

    public List<FunctionMsgChannelConfigResp> getFunctionConfigs(Integer bizFunctionId) {
        //获取默认全部可用的渠道配置
        List<CfBizFunctionMsgResult> all = bizFunctionMsgConfigService.getFunctionConfigs(null, UserInfoContextUtils.getCurrentTenantId());
        //获取当前业务功能已关联的渠道配置
        List<CfBizFunctionMsgResult> list = bizFunctionMsgConfigService.getFunctionConfigs(bizFunctionId, UserInfoContextUtils.getCurrentTenantId());
        if (list.isEmpty()) {
            return BeanUtil.copyToList(all, FunctionMsgChannelConfigResp.class);
        } else {
            list.addAll(all.stream().filter(channel -> !list.stream().map(CfBizFunctionMsgResult::getChannelCode).toList().contains(channel.getChannelCode())).toList());
            return BeanUtil.copyToList(list, FunctionMsgChannelConfigResp.class);
        }
    }

    public Boolean relateBizFunction(BizFunctionMsgConfigAddReq bizFunctionMsgConfig) {
        return bizFunctionMsgConfigService.save(bizFunctionMsgConfig);
    }

}

