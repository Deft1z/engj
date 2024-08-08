package com.kge.energy.crm.repository.dao;

import com.kge.energy.crm.repository.mapper.BDictItemMapper;
import com.kge.energy.crm.repository.entity.BDictItem;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * b_dict_item 字典项表(BDictItem)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class BDictItemDao extends ServiceImpl<BDictItemMapper, BDictItem> {

    private final BDictItemMapper mapper;

}

