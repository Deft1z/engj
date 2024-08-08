package com.kge.energy.crm.repository.dao;

import com.kge.energy.crm.repository.mapper.BDictMapper;
import com.kge.energy.crm.repository.entity.BDict;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * b_dict 字典表(BDict)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class BDictDao extends ServiceImpl<BDictMapper, BDict> {

    private final BDictMapper mapper;

}

