package com.kge.energy.crm.repository.dao;

import com.kge.energy.crm.repository.mapper.RFormUserMapper;
import com.kge.energy.crm.repository.entity.RFormUser;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * r_form_user 表单用户表(RFormUser)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class RFormUserDao extends ServiceImpl<RFormUserMapper, RFormUser> {

    private final RFormUserMapper mapper;

}

