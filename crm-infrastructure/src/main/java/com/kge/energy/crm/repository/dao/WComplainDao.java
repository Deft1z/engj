package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kge.energy.crm.repository.entityext.param.ComplainListParam;
import com.kge.energy.crm.repository.entityext.result.complain.ComplainResult;
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

    public Page<ComplainResult> getComplainList(ComplainListParam param) {
        Page<ComplainResult> page = new Page<>(param.getCurrentPage(), param.getPageSize());
        return mapper.getComplainList(page, param);
    }

    public ComplainResult getComplain(Integer complainId) {
        return mapper.getComplain(complainId);
    }

}

