package com.kge.energy.crm.msg.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.msg.req.BizFunctionMsgConfigAddReq;
import com.kge.energy.crm.msg.resp.SysMsgChannelResp;
import com.kge.energy.crm.repository.dao.SysMsgChannelDao;
import com.kge.energy.crm.repository.entity.SysMsgChannel;
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

    private final SysMsgChannelDao sysMsgChannelDao;

    private final CfBizFunctionMsgConfigService bizFunctionMsgConfigService;

    public List<SysMsgChannelResp> getAll() {
        LambdaQueryWrapper<SysMsgChannel> wrapper = Wrappers.<SysMsgChannel>lambdaQuery()
                .eq(SysMsgChannel::getTenantId, UserInfoContextUtils.getCurrentTenantId())
                .orderByAsc(SysMsgChannel::getId);
        List<SysMsgChannel> list = sysMsgChannelDao.list(wrapper);
        return BeanUtil.copyToList(list, SysMsgChannelResp.class);
    }

    public Boolean relateBizFunction(BizFunctionMsgConfigAddReq bizFunctionMsgConfig) {
        return bizFunctionMsgConfigService.add(bizFunctionMsgConfig);
    }

}

