package com.kge.energy.crm.repository.dao;

import com.kge.energy.crm.repository.mapper.BUserMapper;
import com.kge.energy.crm.repository.entity.BUser;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 用户(BUser)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class BUserDao extends ServiceImpl<BUserMapper, BUser> {

    private final BUserMapper mapper;

}

