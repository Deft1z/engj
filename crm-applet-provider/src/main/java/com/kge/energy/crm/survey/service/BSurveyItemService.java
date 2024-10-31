package com.kge.energy.crm.survey.service;

import com.kge.energy.crm.repository.dao.BSurveyItemDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 调查表单项表(BSurveyItem)Service层
 *
 * @author zhengwenke
 * @since 2024-10-30 09:27:35
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BSurveyItemService {

    private final BSurveyItemDao bSurveyItemDao;

}

