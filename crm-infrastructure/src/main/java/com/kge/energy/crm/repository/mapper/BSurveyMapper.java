package com.kge.energy.crm.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kge.energy.crm.repository.entity.BSurvey;
import com.kge.energy.crm.repository.entityext.result.BSurveyResult;
import org.apache.ibatis.annotations.Param;

/**
 * 调查表单表(BSurvey)数据库访问层
 *
 * @author zhengwenke
 * @since 2024-10-30 09:27:34
 */
public interface BSurveyMapper extends BaseMapper<BSurvey> {

    BSurveyResult getBySurveyCode(@Param("surveyCode") String surveyCode, @Param("tenantId") Integer tenantId);

}

