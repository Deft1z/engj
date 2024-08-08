package com.kge.energy.crm.repository.dao;

import com.kge.energy.crm.repository.mapper.SFileMapper;
import com.kge.energy.crm.repository.entity.SFile;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 文件(SFile)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class SFileDao extends ServiceImpl<SFileMapper, SFile> {

    private final SFileMapper mapper;

}

