package com.kge.energy.crm.iam.service;

import com.kge.energy.crm.repository.dao.IamUserDao;
import com.kge.energy.crm.repository.entity.IamUser;
import com.kge.energy.crm.repository.entityext.result.IamUserResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * iam用户表(IamUser)Service层
 *
 * @author zhengwenke
 * @since 2024-11-11 10:20:38
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class IamUserService {

    private final IamUserDao iamUserDao;

    public IamUser getById(String userId) {
        return iamUserDao.getById(userId);
    }

    @Transactional
    public Boolean insert(IamUser iamUser) {
        LocalDateTime now = LocalDateTime.now();
        iamUser.setSyncCreateTime(now);
        iamUser.setSyncUpdateTime(now);
        return iamUserDao.save(iamUser);
    }

    @Transactional
    public Boolean update(IamUser iamUser) {
        iamUser.setSyncUpdateTime(LocalDateTime.now());
        return iamUserDao.updateById(iamUser);
    }

    public boolean checkHadSync(String userId, String userNormalModifyTimestamp) {
        IamUserResult iamUser = iamUserDao.getByUserId(userId, userNormalModifyTimestamp);
        return iamUser != null;
    }

    public String getLatestModifyTime() {
        String latestModifyTime = iamUserDao.getLatestModifyTime();
        if (StringUtils.isBlank(latestModifyTime)){
            latestModifyTime = "19700101000000Z";
        }
        return latestModifyTime;
    }

}

