package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.repository.entity.BOpenid;
import com.kge.energy.crm.repository.mapper.BOpenidMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 用户对外编码(BOpenid)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class BOpenidDao extends ServiceImpl<BOpenidMapper, BOpenid> {

    private final BOpenidMapper mapper;

    public BOpenid getOpenId(Integer userId, Integer appId) {
        LambdaQueryWrapper<BOpenid> wrapper = Wrappers.<BOpenid>lambdaQuery()
                .eq(BOpenid::getUserId, userId)
                .eq(BOpenid::getAppId, appId)
                .eq(BOpenid::getFlag, 1);
        return mapper.selectOne(wrapper, false);
    }

    public int logicDeleteOpenId(Integer openIdId) {
        LambdaUpdateWrapper<BOpenid> wrapper = Wrappers.<BOpenid>update().lambda()
                .set(BOpenid::getFlag, -1)
                .eq(BOpenid::getOpenidId, openIdId);
        return mapper.update(wrapper);
    }

}

