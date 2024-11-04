package com.kge.energy.crm.survey.service;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kge.energy.crm.common.button.helper.SurveyButtonHelper;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.repository.dao.BSurveyRecordAnswerDao;
import com.kge.energy.crm.repository.dao.BSurveyRecordDao;
import com.kge.energy.crm.repository.entity.BSurveyRecord;
import com.kge.energy.crm.repository.entity.BSurveyRecordAnswer;
import com.kge.energy.crm.survey.resp.SurveyInitResp;
import com.kge.platform.framework.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 调查表单记录填写表(BSurveyRecordAnswer)Service层
 *
 * @author zhengwenke
 * @since 2024-10-30 09:27:36
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BSurveyRecordAnswerService {

    private final BSurveyRecordDao bSurveyRecordDao;

    private final BSurveyRecordAnswerDao bSurveyRecordAnswerDao;

    public SurveyInitResp initAnswer(Integer recordId) {
        BSurveyRecord surveyRecord = bSurveyRecordDao.getById(recordId);
        if (surveyRecord == null) {
            throw new ServiceException("调查表单不存在！");
        }
        UserInfoDto invitee = UserInfoContextUtils.getCurrentUserInfo();
        BSurveyRecordAnswer answer = getInviteeAnswer(recordId, invitee.getUserId().intValue());
        //已填写，直接返回填写记录
        if (answer == null) {
            //未填写，新增记录
            answer = new BSurveyRecordAnswer().setPromoterId(surveyRecord.getCreateUserId())
                    .setInviteeId(invitee.getUserId().intValue())
                    .setSurveyRecordId(recordId)
                    .setSurveyName(surveyRecord.getSurveyName())
                    .setFillJson(surveyRecord.getFillJson())
                    .setTenantId(invitee.getTenantId());
            bSurveyRecordAnswerDao.save(answer);
        }
        SurveyInitResp resp = JSONUtil.toBean(answer.getFillJson(), SurveyInitResp.class);
        resp.setButtons(SurveyButtonHelper.getButtons(surveyRecord, invitee.getUserId().intValue()));
        return resp;
    }

    /**
     *
     * 获取调查受邀请人的填写答复
     * @param recordId
     * @param inviteeId
     * @return
     */
    private BSurveyRecordAnswer getInviteeAnswer(Integer recordId, Integer inviteeId) {
        LambdaQueryWrapper<BSurveyRecordAnswer> queryWrapper = Wrappers.<BSurveyRecordAnswer>lambdaQuery()
                .eq(BSurveyRecordAnswer::getSurveyRecordId, recordId)
                .eq(BSurveyRecordAnswer::getInviteeId, inviteeId);
        return bSurveyRecordAnswerDao.getOne(queryWrapper);
    }

    @Transactional
    public Boolean save(SurveyInitResp req) {
        Integer inviteeId = UserInfoContextUtils.getCurrentUserId();
        BSurveyRecordAnswer answer = getInviteeAnswer(req.getRecordId(), inviteeId);
        if (inviteeId.equals(answer.getPromoterId())){
            throw new ServiceException("调查发起人不能填写自己发起的调查表！");
        }
        //1 待评价 2 已评价
        if (answer.getStatus().equals(2)) {
            throw new ServiceException("已完成调查填写，无需重复提交！");
        }
        answer.setFillJson(JSONUtil.toJsonStr(req));
        if (Boolean.TRUE.equals(req.getSubmitFlag())) {
            Integer status = 2;
            answer.setStatus(status);
            //同步更新调查记录表的状态 ，将待评价status=1更新为已评价status=2
            bSurveyRecordDao.update(Wrappers.<BSurveyRecord>lambdaUpdate()
                    .set(BSurveyRecord::getStatus, status)
                    .eq(BSurveyRecord::getId, req.getRecordId())
                    .eq(BSurveyRecord::getStatus, 1)
            );
        }
        return bSurveyRecordAnswerDao.updateById(answer);
    }

}

