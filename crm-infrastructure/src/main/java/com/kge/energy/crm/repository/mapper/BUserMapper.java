package com.kge.energy.crm.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.repository.entity.BUser;
import com.kge.energy.crm.repository.entityext.result.RoleUserResult;
import com.kge.energy.crm.repository.entityext.result.UserBindByMobileResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户(BUser)表数据库接口层
 */
public interface BUserMapper extends BaseMapper<BUser> {

    UserInfoDto findUserInfoDto(Integer userId);

    List<RoleUserResult> getUserByRoleAndOrgId(@Param("roleId") Integer roleId, @Param("organizationId") Integer organizationId);

    List<RoleUserResult> getUserByRoleId(@Param("roleId") Integer roleId);

    List<BUser> findUserByCurrentOrgId(Integer orgId);

    List<UserBindByMobileResult> findUserBindByUid(@Param("uid") Integer uid);



}

