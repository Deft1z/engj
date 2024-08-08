package com.kge.energy.crm.repository.dao;

import com.kge.energy.crm.repository.mapper.BRoleMapper;
import com.kge.energy.crm.repository.entity.BRole;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 角色(BRole)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class BRoleDao extends ServiceImpl<BRoleMapper, BRole> {

    private final BRoleMapper mapper;

}

