package com.kge.energy.crm.repository.dao;

import com.kge.energy.crm.repository.mapper.RUserRoleMapper;
import com.kge.energy.crm.repository.entity.RUserRole;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 用户角色关系(RUserRole)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class RUserRoleDao extends ServiceImpl<RUserRoleMapper, RUserRole> {

    private final RUserRoleMapper mapper;

}

