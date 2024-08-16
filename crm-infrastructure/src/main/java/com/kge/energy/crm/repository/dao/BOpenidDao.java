package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kge.energy.crm.repository.entity.ROpenidProject;
import com.kge.energy.crm.repository.mapper.BOpenidMapper;
import com.kge.energy.crm.repository.entity.BOpenid;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.sql.Wrapper;

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
        int resultInt = mapper.update(wrapper);
        if (resultInt == 0) {
            return 0;
        }
        return 1;
    }

}

