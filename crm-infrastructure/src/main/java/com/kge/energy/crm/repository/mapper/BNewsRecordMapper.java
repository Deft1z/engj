package com.kge.energy.crm.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kge.energy.crm.repository.entity.BNewsRecord;
import com.kge.energy.crm.repository.entityext.param.news.PageNewsParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 新闻记录表(BNewsRecord)表数据库接口层
 */
public interface BNewsRecordMapper extends BaseMapper<BNewsRecord> {

    List<BNewsRecord> indexAllChannelNews(Integer pageSize);

    IPage<BNewsRecord> pageNews(Page<BNewsRecord> page, @Param("param") PageNewsParam param);
}

