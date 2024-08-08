package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.repository.entity.LUserToken;
import com.kge.energy.crm.repository.mapper.LUserTokenMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Objects;

/**
 * 用户令牌(LUserToken)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class LUserTokenDao extends ServiceImpl<LUserTokenMapper, LUserToken> {

    private final LUserTokenMapper mapper;

    public LUserToken findByUid(Integer userId) {
        if (Objects.equals(userId, 0)) {
            return null;
        }

        LambdaQueryWrapper<LUserToken> wrapper = Wrappers.<LUserToken>lambdaQuery()
                .eq(LUserToken::getUserId, userId);

        return mapper.selectOne(wrapper);
    }
}

