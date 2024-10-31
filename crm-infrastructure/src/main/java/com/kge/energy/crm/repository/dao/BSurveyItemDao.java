package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.repository.entity.BSurveyItem;
import com.kge.energy.crm.repository.mapper.BSurveyItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 调查表单项表(BSurveyItem)数据库访问层
 *
 * @author zhengwenke
 * @since 2024-10-30 09:27:35
 */
@Repository
@RequiredArgsConstructor
public class BSurveyItemDao extends ServiceImpl<BSurveyItemMapper, BSurveyItem> {

    private final BSurveyItemMapper mapper;

}

