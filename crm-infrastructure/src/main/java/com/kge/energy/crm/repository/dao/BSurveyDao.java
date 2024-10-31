package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.repository.entity.BSurvey;
import com.kge.energy.crm.repository.entityext.result.BSurveyResult;
import com.kge.energy.crm.repository.mapper.BSurveyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 调查表单表(BSurvey)数据库访问层
 *
 * @author zhengwenke
 * @since 2024-10-30 09:27:34
 */
@Repository
@RequiredArgsConstructor
public class BSurveyDao extends ServiceImpl<BSurveyMapper, BSurvey> {

    private final BSurveyMapper mapper;

    public BSurveyResult getBySurveyCode(String surveyCode, Integer tenantId) {
        return mapper.getBySurveyCode(surveyCode, tenantId);
    }

}

