package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.repository.entity.RFormUser;
import com.kge.energy.crm.repository.mapper.RFormUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * r_form_user 表单用户表(RFormUser)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class RFormUserDao extends ServiceImpl<RFormUserMapper, RFormUser> {

    private final RFormUserMapper mapper;

}

