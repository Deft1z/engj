package com.kge.energy.crm.repository.mapper;

import com.kge.energy.crm.repository.entity.BOpenidShare;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kge.energy.crm.repository.entityext.result.OpenidShareBindResult;

import java.util.List;

/**
 * 对外编码共享表(BOpenidShare)表数据库接口层
 */
public interface BOpenidShareMapper extends BaseMapper<BOpenidShare> {

    public List<OpenidShareBindResult> getShareBind(Integer uid);
    
}

