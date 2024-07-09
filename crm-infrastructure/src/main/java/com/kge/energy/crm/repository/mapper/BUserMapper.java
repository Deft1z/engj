package com.kge.energy.crm.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.repository.entity.BUser;

/**
 * 用户(BUser)表数据库接口层
 */
public interface BUserMapper extends BaseMapper<BUser> {

    UserInfoDto findUserInfoDto(Integer userId);
}

