package com.kge.energy.crm.user.service;

import com.kge.energy.crm.repository.dao.BUserDao;
import com.kge.energy.crm.repository.entity.BUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author wangjihua
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BUserService {

    private final BUserDao bUserDao;

    public BUser getBUserById(int id) {
        return bUserDao.getById(id);
    }
}
