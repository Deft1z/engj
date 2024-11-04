package com.kge.energy.crm.survey.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kge.energy.crm.common.button.helper.SurveyButtonHelper;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.repository.dao.BSurveyDao;
import com.kge.energy.crm.repository.entity.BSurvey;
import com.kge.energy.crm.repository.entityext.result.BSurveyResult;
import com.kge.energy.crm.survey.resp.SurveyInitResp;
import com.kge.energy.crm.survey.resp.SurveyResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 调查表单表(BSurvey)Service层
 *
 * @author zhengwenke
 * @since 2024-10-30 09:27:34
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BSurveyService {

    private final BSurveyDao bSurveyDao;

    public List<SurveyResult> getAll() {
        List<BSurvey> list = bSurveyDao.list(Wrappers.<BSurvey>lambdaQuery()
                .eq(BSurvey::getTenantId, UserInfoContextUtils.getCurrentTenantId())
        );
        return BeanUtil.copyToList(list, SurveyResult.class);
    }

    public SurveyInitResp getBySurveyCode(String surveyCode) {
        UserInfoDto operator = UserInfoContextUtils.getCurrentUserInfo();
        BSurveyResult survey = bSurveyDao.getBySurveyCode(surveyCode, operator.getTenantId());
        SurveyInitResp resp = BeanUtil.copyProperties(survey, SurveyInitResp.class);
        resp.setSurveyId(survey.getId());
        resp.setButtons(SurveyButtonHelper.getButtons(null, operator.getUserId().intValue()));
        //遍历表单项是否可编辑填写
        resp.setLockedSurveyItem(null, operator.getUserId().intValue(), resp.getSurveyItems());
        return resp;
    }

}

