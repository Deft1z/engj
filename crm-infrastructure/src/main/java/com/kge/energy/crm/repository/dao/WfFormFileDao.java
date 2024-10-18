package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.repository.entity.WfFormFile;
import com.kge.energy.crm.repository.mapper.WfFormFileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 表单附件(WfFormFile)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class WfFormFileDao extends ServiceImpl<WfFormFileMapper, WfFormFile> {

    private final WfFormFileMapper mapper;

}

