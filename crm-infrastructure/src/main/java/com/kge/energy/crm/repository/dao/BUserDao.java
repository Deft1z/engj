package com.kge.energy.crm.repository.dao;


import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.repository.entity.BUser;
import com.kge.energy.crm.repository.mapper.BUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Objects;

/**
 * 用户(BUser)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class BUserDao extends ServiceImpl<BUserMapper, BUser> {

    private final BUserMapper mapper;


    public BUser FindUserByMobile(String openId) {
        if (Objects.equals(openId, "")) {
            return null;
        }

        LambdaQueryWrapper<BUser> wrapper = Wrappers.<BUser>lambdaQuery()
                .eq(BUser::getOpenId, openId).eq(BUser::getFlag, 1);

        return mapper.selectOne(wrapper);
    }


    public UserInfoDto findUserInfoDto(Integer userId) {
        Assert.notNull(userId, "userId must not be null");
        return mapper.findUserInfoDto(userId);
    }
}

