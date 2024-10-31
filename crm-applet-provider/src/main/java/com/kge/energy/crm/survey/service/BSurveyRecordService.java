package com.kge.energy.crm.survey.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.common.util.AppletLinkUtils;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.external.wechat.applet.service.WeChatAppletInfraService;
import com.kge.energy.crm.repository.dao.BSurveyRecordAnswerDao;
import com.kge.energy.crm.repository.dao.BSurveyRecordDao;
import com.kge.energy.crm.repository.entity.BSurveyRecord;
import com.kge.energy.crm.repository.entity.BSurveyRecordAnswer;
import com.kge.energy.crm.repository.entityext.param.SurveyRecordParam;
import com.kge.energy.crm.repository.entityext.result.BSurveyRecordResult;
import com.kge.energy.crm.survey.req.SurveyRecordReq;
import com.kge.energy.crm.survey.resp.SurveyInitResp;
import com.kge.energy.crm.survey.resp.SurveyRecordResp;
import com.kge.platform.framework.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 调查表单记录表(BSurveyRecord)Service层
 *
 * @author zhengwenke
 * @since 2024-10-30 09:27:36
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BSurveyRecordService {

    private final BSurveyRecordDao bSurveyRecordDao;

    private final BSurveyRecordAnswerDao bSurveyRecordAnswerDao;

    private final WeChatAppletInfraService weChatAppletInfraService;

    public PageResp<SurveyRecordResp> getByPage(SurveyRecordReq req) {
        SurveyRecordParam param = BeanUtil.copyProperties(req, SurveyRecordParam.class);

        UserInfoDto userInfoDto = UserInfoContextUtils.getCurrentUserInfo();
        param.setUserId(userInfoDto.getUserId().intValue());
        param.setTenantId(userInfoDto.getTenantId());

        IPage<BSurveyRecordResult> pages = bSurveyRecordDao.getByPage(param);

        return new PageResp<SurveyRecordResp>().setList(BeanUtil.copyToList(pages.getRecords(), SurveyRecordResp.class))
                .setCurrentPage(pages.getCurrent())
                .setPageSize(pages.getSize())
                .setTotal(pages.getTotal());
    }

    public SurveyInitResp getById(Integer id) {
        Integer userId = UserInfoContextUtils.getCurrentUserId();
        String fillJson;
        //查询是否有调查答复
        List<BSurveyRecordAnswer> answers = bSurveyRecordAnswerDao.list(Wrappers.<BSurveyRecordAnswer>lambdaQuery()
                .eq(BSurveyRecordAnswer::getSurveyRecordId, id)
                .and(i -> i.eq(BSurveyRecordAnswer::getPromoterId, userId).or().eq(BSurveyRecordAnswer::getInviteeId, userId))
                .orderByDesc(BSurveyRecordAnswer::getCreateTime)
        );
        //有调查答复则返回已填写的调查表
        if (!answers.isEmpty()) {
            fillJson = answers.get(0).getFillJson();
        } else {
            //无调查答复则返回未填写的调查表
            fillJson = bSurveyRecordDao.getById(id).getFillJson();
        }

        return JSONUtil.toBean(fillJson, SurveyInitResp.class);
    }

    @Transactional
    public Boolean save(SurveyInitResp req) {
        BSurveyRecord surveyRecord;
        Integer recordId = req.getRecordId();
        if (recordId == null) {
            //新增记录
            surveyRecord = new BSurveyRecord().setTenantId(UserInfoContextUtils.getCurrentTenantId());
            BeanUtil.copyProperties(req, surveyRecord);
            bSurveyRecordDao.save(surveyRecord);
            req.setRecordId(surveyRecord.getId());
        } else {
            surveyRecord = bSurveyRecordDao.getById(recordId);
            // 0 未提交 1 待评价 2 已评价 3 已完成
            if (!surveyRecord.getStatus().equals(0)) {
                throw new ServiceException("表单已提交，无法修改！");
            }
        }
        //编辑记录
        generateShareUrl(req.getSubmitFlag(), surveyRecord);
        surveyRecord.setFillJson(JSONUtil.toJsonStr(req));
        return bSurveyRecordDao.updateById(surveyRecord);
    }

    private void generateShareUrl(Boolean submitFlag, BSurveyRecord surveyRecord) {
        if (Boolean.TRUE.equals(submitFlag)) {
            // 0 未提交 1 待评价 2 已评价 3 已完成
            surveyRecord.setStatus(1);
            Integer expireDays = 30;
            surveyRecord.setShareUrl(weChatAppletInfraService.getWeChatAppletUrlLink(null, AppletLinkUtils.getSurveyAnswerQuery(surveyRecord.getId()), expireDays));
            surveyRecord.setShareExpireAt(LocalDateTime.now().plusDays(expireDays));
        }
    }

}

