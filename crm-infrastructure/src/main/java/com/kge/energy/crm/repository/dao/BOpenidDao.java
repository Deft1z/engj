package com.kge.energy.crm.repository.dao;

import com.kge.energy.crm.repository.mapper.BOpenidMapper;
import com.kge.energy.crm.repository.entity.BOpenid;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 用户对外编码(BOpenid)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class BOpenidDao extends ServiceImpl<BOpenidMapper, BOpenid> {

    private final BOpenidMapper mapper;

}

