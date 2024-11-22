package com.kge.energy.crm.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kge.energy.crm.repository.entity.BNewsChannel;
import com.kge.energy.crm.repository.entityext.result.news.NewsChannelResult;

import java.util.List;

/**
 * 新闻渠道配置表(BNewsChannel)表数据库接口层
 */
public interface BNewsChannelMapper extends BaseMapper<BNewsChannel> {

    List<NewsChannelResult> channels();
}

