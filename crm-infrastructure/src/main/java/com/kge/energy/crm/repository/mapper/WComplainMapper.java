package com.kge.energy.crm.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kge.energy.crm.repository.entity.WComplain;
import org.apache.ibatis.annotations.Param;

/**
 * 投诉反馈(WComplain)表数据库接口层
 */
public interface WComplainMapper extends BaseMapper<WComplain> {

    public Long findNewComplainCount(@Param("startTime") String startTime, @Param("endTime") String endTime);
}

