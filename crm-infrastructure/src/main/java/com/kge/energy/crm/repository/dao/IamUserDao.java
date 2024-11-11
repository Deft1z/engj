package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.repository.entity.IamUser;
import com.kge.energy.crm.repository.entityext.result.IamUserResult;
import com.kge.energy.crm.repository.mapper.IamUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * iam用户表(IamUser)数据库访问层
 *
 * @author zhengwenke
 * @since 2024-11-11 10:20:38
 */
@Repository
@RequiredArgsConstructor
public class IamUserDao extends ServiceImpl<IamUserMapper, IamUser> {

    private final IamUserMapper mapper;

    public IamUserResult getBySimId(String simId, String modifyTime) {
        return mapper.getBySimId(simId, modifyTime);
    }

    public String getLatestModifyTime() {
        return mapper.getLatestModifyTime();
    }

}

