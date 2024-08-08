package com.kge.energy.crm.repository.dao;

import com.kge.energy.crm.repository.mapper.WfFormFileMapper;
import com.kge.energy.crm.repository.entity.WfFormFile;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 表单附件(WfFormFile)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class WfFormFileDao extends ServiceImpl<WfFormFileMapper, WfFormFile> {

    private final WfFormFileMapper mapper;

}

