package com.kge.energy.crm.repository.dao;

import com.kge.energy.crm.repository.mapper.LUserTokenMapper;
import com.kge.energy.crm.repository.entity.LUserToken;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 用户令牌(LUserToken)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class LUserTokenDao extends ServiceImpl<LUserTokenMapper, LUserToken> {

    private final LUserTokenMapper mapper;

}

