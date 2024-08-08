package com.kge.energy.crm.repository.dao;

import com.kge.energy.crm.repository.mapper.RFormConsultComplainMapper;
import com.kge.energy.crm.repository.entity.RFormConsultComplain;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * r_form_consult_complain 咨询投诉工单表(RFormConsultComplain)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class RFormConsultComplainDao extends ServiceImpl<RFormConsultComplainMapper, RFormConsultComplain> {

    private final RFormConsultComplainMapper mapper;

}

