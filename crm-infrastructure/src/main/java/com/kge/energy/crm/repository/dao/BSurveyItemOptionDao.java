package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.repository.entity.BSurveyItemOption;
import com.kge.energy.crm.repository.mapper.BSurveyItemOptionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 调查表单项选值表(BSurveyItemOption)数据库访问层
 *
 * @author zhengwenke
 * @since 2024-10-30 09:27:35
 */
@Repository
@RequiredArgsConstructor
public class BSurveyItemOptionDao extends ServiceImpl<BSurveyItemOptionMapper, BSurveyItemOption> {

    private final BSurveyItemOptionMapper mapper;

}

