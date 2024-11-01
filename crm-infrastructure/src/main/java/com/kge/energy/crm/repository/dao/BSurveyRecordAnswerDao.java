package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.repository.entity.BSurveyRecordAnswer;
import com.kge.energy.crm.repository.mapper.BSurveyRecordAnswerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 调查表单记录填写表(BSurveyRecordAnswer)数据库访问层
 *
 * @author zhengwenke
 * @since 2024-10-30 09:27:36
 */
@Repository
@RequiredArgsConstructor
public class BSurveyRecordAnswerDao extends ServiceImpl<BSurveyRecordAnswerMapper, BSurveyRecordAnswer> {

    private final BSurveyRecordAnswerMapper mapper;

}

