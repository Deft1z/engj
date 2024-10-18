package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.repository.entity.RFormConsultComplain;
import com.kge.energy.crm.repository.mapper.RFormConsultComplainMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * r_form_consult_complain 咨询投诉工单表(RFormConsultComplain)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class RFormConsultComplainDao extends ServiceImpl<RFormConsultComplainMapper, RFormConsultComplain> {

    private final RFormConsultComplainMapper mapper;

}

