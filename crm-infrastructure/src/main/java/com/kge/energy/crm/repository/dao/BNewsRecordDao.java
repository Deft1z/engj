package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.repository.entity.BNewsRecord;
import com.kge.energy.crm.repository.entityext.param.news.PageNewsParam;
import com.kge.energy.crm.repository.mapper.BNewsRecordMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 新闻记录表(BNewsRecord)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class BNewsRecordDao extends ServiceImpl<BNewsRecordMapper, BNewsRecord> {

    private final BNewsRecordMapper mapper;

    public List<BNewsRecord> indexAllChannelNews(Integer pageSize) {
        return mapper.indexAllChannelNews(pageSize);
    }

    public IPage<BNewsRecord> pageNews(PageNewsParam param) {
        Page<BNewsRecord> page = new Page<>(param.getCurrentPage(), param.getPageSize());
        return mapper.pageNews(page, param);
    }

    public void saveUploadNews(List<BNewsRecord> bNewsRecords) {
        mapper.saveUploadNews(bNewsRecords);
    }
}

