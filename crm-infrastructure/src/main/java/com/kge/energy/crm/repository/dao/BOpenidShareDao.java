package com.kge.energy.crm.repository.dao;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.repository.entity.BOpenidShare;
import com.kge.energy.crm.repository.entityext.result.OpenidShareBindResult;
import com.kge.energy.crm.repository.mapper.BOpenidShareMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 对外编码共享表(BOpenidShare)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class BOpenidShareDao extends ServiceImpl<BOpenidShareMapper, BOpenidShare> {

    private final BOpenidShareMapper mapper;

    public Integer getShareBind(Integer uid, Integer appid) {
        List<OpenidShareBindResult> results = mapper.getShareBind(uid);
        for (OpenidShareBindResult result : results) {
            if (NumberUtil.equals(result.getAppid(), appid)) {
                return result.getOpenid();
            }
        }
        return 0;
    }

}

