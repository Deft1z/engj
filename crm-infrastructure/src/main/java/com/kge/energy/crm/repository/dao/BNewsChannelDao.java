package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.repository.entity.BNewsChannel;
import com.kge.energy.crm.repository.entityext.result.news.NewsChannelResult;
import com.kge.energy.crm.repository.mapper.BNewsChannelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 新闻渠道配置表(BNewsChannel)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class BNewsChannelDao extends ServiceImpl<BNewsChannelMapper, BNewsChannel> {

    private final BNewsChannelMapper mapper;

    public List<NewsChannelResult> channels() {
        return mapper.channels();
    }
}

