package com.kge.energy.crm.repository.dao;

import com.kge.energy.crm.repository.mapper.LValidateCodeMapper;
import com.kge.energy.crm.repository.entity.LValidateCode;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 校验码(LValidateCode)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class LValidateCodeDao extends ServiceImpl<LValidateCodeMapper, LValidateCode> {

    private final LValidateCodeMapper mapper;

}

