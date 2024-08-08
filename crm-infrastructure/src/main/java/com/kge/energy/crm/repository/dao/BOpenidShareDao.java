package com.kge.energy.crm.repository.dao;

import com.kge.energy.crm.repository.mapper.BOpenidShareMapper;
import com.kge.energy.crm.repository.entity.BOpenidShare;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 对外编码共享表(BOpenidShare)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class BOpenidShareDao extends ServiceImpl<BOpenidShareMapper, BOpenidShare> {

    private final BOpenidShareMapper mapper;

}

