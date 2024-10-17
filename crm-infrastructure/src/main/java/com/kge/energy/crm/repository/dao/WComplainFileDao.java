package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.repository.entity.WComplainFile;
import com.kge.energy.crm.repository.mapper.WComplainFileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 投诉附件(WComplainFile)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class WComplainFileDao extends ServiceImpl<WComplainFileMapper, WComplainFile> {

    private final WComplainFileMapper mapper;

}

