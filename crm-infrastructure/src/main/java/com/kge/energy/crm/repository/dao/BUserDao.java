package com.kge.energy.crm.repository.dao;


import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.repository.entity.BUser;
import com.kge.energy.crm.repository.entityext.param.UserListParam;
import com.kge.energy.crm.repository.entityext.result.RoleUserResult;
import com.kge.energy.crm.repository.entityext.result.UserListResult;
import com.kge.energy.crm.repository.mapper.BUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

/**
 * 用户(BUser)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class BUserDao extends ServiceImpl<BUserMapper, BUser> {

    private final BUserMapper mapper;

    public BUser findUserByOpenId(String openId) {
        if (Objects.equals(openId, "")) {
            return null;
        }

        LambdaQueryWrapper<BUser> wrapper = Wrappers.<BUser>lambdaQuery()
                .eq(BUser::getOpenId, openId);

        return mapper.selectOne(wrapper);
    }


    public List<UserInfoDto.Role> getUserRoles(String systemType, Integer userId) {
        Assert.notBlank(systemType, "systemType must not be null or blank");
        Assert.notNull(userId, "userId must not be null");
        return mapper.getUserRoles(systemType, userId);
    }

    public List<RoleUserResult> getUserByRoleId(Integer roleId) {
        Assert.notNull(roleId, "roleId must not be null");
        return mapper.getUserByRoleId(roleId);
    }

    public List<RoleUserResult> getUserByRoleAndOrgId(Integer roleId, Integer organizationId) {
        Assert.notNull(roleId, "roleId must not be null");
        Assert.notNull(organizationId, "organizationId must not be null");
        return mapper.getUserByRoleAndOrgId(roleId, organizationId);
    }

    public List<BUser> findUserByCurrentOrgId(Integer currentOrgId) {
        Assert.notNull(currentOrgId, "currentOrgId must not be null");
        return mapper.findUserByCurrentOrgId(currentOrgId);
    }

    public BUser findOneByName(String name) {
        LambdaQueryWrapper<BUser> wrapper = Wrappers.<BUser>lambdaQuery()
                .eq(BUser::getName, name);

        return mapper.selectOne(wrapper);
    }

    public BUser getUserByMobile(String mobile) {
        LambdaQueryWrapper<BUser> wrapper = Wrappers.<BUser>lambdaQuery()
                .eq(BUser::getMobile, mobile)
                .eq(BUser::getFlag, 1);
        return mapper.selectOne(wrapper);
    }

    public IPage<BUser> findAllWxUser(String name, Long currentPage, Long pageSize) {
        QueryWrapper<BUser> wrapper = Wrappers.query();
        // 封装分页信息
        if (currentPage == null || pageSize == null) {
            currentPage = 1L;
            pageSize = 10L;
        }
        Page<BUser> page = new Page<>(currentPage, pageSize);
        wrapper.and(w -> w.eq("type", "社会客户").or().eq("type", "领导"));
        if (name == null) {
            return mapper.selectPage(page, wrapper);
        }
        wrapper.like("name", name).or().eq("mobile", name).or().like("realname", name).or().like("company", name);
        return mapper.selectPage(page, wrapper);
    }

    public List<BUser> findByPhone(String phone) {
        LambdaQueryWrapper<BUser> wrapper = Wrappers.<BUser>lambdaQuery()
                .eq(BUser::getMobile, phone)
                .eq(BUser::getFlag, 1);
        return mapper.selectList(wrapper);
    }

    public String findShareUser(List<Integer> userIdList, Integer appid) {
        return mapper.findShareUser(userIdList, appid);
    }

    public Long findNewUserNum(String startTime, String endTime) {
        return mapper.findNewUserNum(startTime, endTime);
    }

    public Long findNewUserCount(String startTime, String endTime) {
        return mapper.findNewUserCount(startTime, endTime);
    }

    public IPage<UserListResult> list(UserListParam param) {
        Page<UserListResult> page = new Page<>(param.getCurrentPage(), param.getPageSize());
        return mapper.list(page, param);
    }

    public List<String> findJtCustomerPhones() {
        return mapper.findJtCustomerPhones();
    }

    public List<String> findSubCustomerPhones(Integer orgId) {
        return mapper.findSubCustomerPhones(orgId);
    }

}

