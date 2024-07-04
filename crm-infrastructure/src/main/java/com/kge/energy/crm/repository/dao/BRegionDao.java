package com.kge.energy.crm.repository.dao;

import com.kge.energy.crm.repository.mapper.BRegionMapper;
import com.kge.energy.crm.repository.entity.BRegion;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 行政区域(BRegion)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class BRegionDao extends ServiceImpl<BRegionMapper, BRegion> {

    private final BRegionMapper mapper;

}

