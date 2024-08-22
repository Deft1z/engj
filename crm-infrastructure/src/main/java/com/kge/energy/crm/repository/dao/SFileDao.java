package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.repository.entity.SFile;
import com.kge.energy.crm.repository.mapper.SFileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 文件(SFile)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class SFileDao extends ServiceImpl<SFileMapper, SFile> {

    private final SFileMapper mapper;

    public SFile getByFilePath(String filePath) {

        Wrapper<SFile> wrapper = new LambdaQueryWrapper<SFile>()
                .eq(SFile::getFilepath, filePath);

        return mapper.selectOne(wrapper);
    }
}

