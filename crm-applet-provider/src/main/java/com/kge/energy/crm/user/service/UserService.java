package com.kge.energy.crm.user.service;

import com.kge.energy.crm.repository.dao.BUserDao;
import com.kge.energy.crm.repository.entity.BUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final BUserDao bUserDao;

    public List<BUser> findByPhone(String phone) {
        return bUserDao.findByPhone(phone);
    }

    public String findShareUser(List<Integer> userIdList, Integer appid) {
        return bUserDao.findShareUser(userIdList, appid);
    }
}
