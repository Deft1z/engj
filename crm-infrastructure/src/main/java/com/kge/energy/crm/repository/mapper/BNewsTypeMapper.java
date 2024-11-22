package com.kge.energy.crm.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kge.energy.crm.repository.entity.BNewsType;
import org.apache.ibatis.annotations.Param;

/**
 * 新闻类型配置表(BNewsType)表数据库接口层
 */
public interface BNewsTypeMapper extends BaseMapper<BNewsType> {

    BNewsType selectOneByCode(@Param("channelCode") String channelCode, @Param("typeCode") String typeCode);
}

