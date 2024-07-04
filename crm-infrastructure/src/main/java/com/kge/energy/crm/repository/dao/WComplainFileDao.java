package com.kge.energy.crm.repository.dao;

import com.kge.energy.crm.repository.mapper.WComplainFileMapper;
import com.kge.energy.crm.repository.entity.WComplainFile;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 投诉附件(WComplainFile)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class WComplainFileDao extends ServiceImpl<WComplainFileMapper, WComplainFile> {

    private final WComplainFileMapper mapper;

}

