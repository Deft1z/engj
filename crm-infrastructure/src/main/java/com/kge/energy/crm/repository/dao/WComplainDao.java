package com.kge.energy.crm.repository.dao;

import com.kge.energy.crm.repository.mapper.WComplainMapper;
import com.kge.energy.crm.repository.entity.WComplain;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 投诉反馈(WComplain)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class WComplainDao extends ServiceImpl<WComplainMapper, WComplain> {

    private final WComplainMapper mapper;

    public Long findComplainCount(String startTime, String endTime) {
        return mapper.findNewComplainCount(startTime, endTime);
    }

}

