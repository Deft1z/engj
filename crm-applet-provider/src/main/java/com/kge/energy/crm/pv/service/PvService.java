package com.kge.energy.crm.pv.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.common.net.ResponseCode;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.external.epcpv.req.EpcpvDetailsCondition;
import com.kge.energy.crm.external.epcpv.req.EpcpvDetailsReq;
import com.kge.energy.crm.external.epcpv.req.EpcpvInfoReq;
import com.kge.energy.crm.external.epcpv.service.EpcpvService;
import com.kge.energy.crm.pv.req.*;
import com.kge.energy.crm.pv.resp.AppletCommentResp;
import com.kge.energy.crm.repository.dao.CmsCommentDao;
import com.kge.energy.crm.repository.dao.RUserLikeCommentDao;
import com.kge.energy.crm.repository.entity.CmsComment;
import com.kge.energy.crm.repository.entity.RUserLikeComment;
import com.kge.energy.crm.repository.entityext.result.AppletCommentResult;
import com.kge.platform.framework.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PvService {

    private final EpcpvService epcpvService;

    private final CmsCommentDao commentDao;

    private final RUserLikeCommentDao userLikeCommentDao;

    public Map<String, Object> getAllPvInfo(PvInfoReq pvInfoReq) {
        EpcpvInfoReq epcpvInfoReq = new EpcpvInfoReq(pvInfoReq.getStartdate(), pvInfoReq.getEnddate());
        Map<String, Object> resultMap = epcpvService.getAllPvInfo(epcpvInfoReq);

        //获取评论列表
        List<AppletCommentResp> comments = new ArrayList<>();
        Integer currentUserId = UserInfoContextUtils.getCurrentUserId();
        List<AppletCommentResult> commentList = commentDao.getAllCommentsByUserId(currentUserId);
        for (AppletCommentResult appletCommentResult : commentList) {
            AppletCommentResp appletCommentResp = new AppletCommentResp();
            BeanUtil.copyProperties(appletCommentResult, appletCommentResp);
            appletCommentResp.setRealname(appletCommentResult.getName());

            if (appletCommentResult.getCalLikeNum() != appletCommentResult.getThumb()) {
                boolean updateResult = commentDao.updateLikeNumber(appletCommentResult.getId(), appletCommentResult.getCalLikeNum());
                if (updateResult) {
                    appletCommentResp.setThumb(appletCommentResult.getCalLikeNum());
                }
            }
            comments.add(appletCommentResp);
        }
        resultMap.put("comments", comments);

        return resultMap;
    }

    public Integer commentPv(PvCommentReq pvCommentReq) {
        CmsComment cmsComment = new CmsComment();
        cmsComment.setUserId(UserInfoContextUtils.getCurrentUserId());
        cmsComment.setContent(pvCommentReq.getContent());

        if (pvCommentReq.getId() > 0) {
            cmsComment.setParentCommentId(pvCommentReq.getId());
        }
        commentDao.save(cmsComment);

        return cmsComment.getCommentId();
    }

    @Transactional
    public Boolean commentPvDel(PvCommentDelReq pvCommentDelReq) {
        Integer userId = UserInfoContextUtils.getCurrentUserId();
        CmsComment cmsComment = commentDao.getOne(Wrappers.lambdaQuery(new CmsComment().setCommentId(pvCommentDelReq.getId()).setUserId(userId)));
        if (ObjectUtil.isNull(cmsComment)) {
            throw new ServiceException("评论不存在");
        }

        Boolean lResult = userLikeCommentDao.removeById(new RUserLikeComment().setCommentId(pvCommentDelReq.getId()));

        Boolean cResult = commentDao.removeById(pvCommentDelReq.getId());

        return lResult && cResult;
    }

    public boolean likeComment(PvLikeReq pvLikeReq) {
        UserInfoDto userInfoDto = UserInfoContextUtils.getCurrentUserInfo();
        if (ObjectUtil.isNull(userInfoDto)) {
            throw new ServiceException(ResponseCode.AUTHORITY_FAIL.getMsg());
        }

        CmsComment cmsComment = commentDao.getById(pvLikeReq.getId());
        if (ObjectUtil.isNull(cmsComment)) {
            return false;
        }

        if (pvLikeReq.getStatus() == 0) { // 点赞
            RUserLikeComment userLikeComment = new RUserLikeComment();
            userLikeComment.setCommentId(pvLikeReq.getId());
            userLikeComment.setUserId(userInfoDto.getUserId().intValue());
            userLikeComment.setFlag(1);

            if (userLikeCommentDao.findThumbsUp(userLikeComment)) {// 查找之前是否已经点赞了
                return false;
            }

            if (userLikeCommentDao.findThumbsUp2(userLikeComment)) {// 之前点赞过，但又取消的
                boolean result = userLikeCommentDao.thumbsUp2(userLikeComment);
                if (result) {
                    return commentDao.thumbsUp(pvLikeReq.getId());
                }
                return false;
            }

            boolean result = userLikeCommentDao.save(userLikeComment);
            if (result) {
                return commentDao.thumbsUp(pvLikeReq.getId());
            }

            return false;

        } else { //取消点赞
            RUserLikeComment userLikeComment = new RUserLikeComment();
            userLikeComment.setCommentId(pvLikeReq.getId());
            userLikeComment.setUserId(userInfoDto.getUserId().intValue());

            LambdaQueryWrapper<RUserLikeComment> wrapper = Wrappers.<RUserLikeComment>lambdaQuery()
                    .eq(RUserLikeComment::getCommentId, userLikeComment.getCommentId())
                    .eq(RUserLikeComment::getUserId, userLikeComment.getUserId());
            Long cnt = userLikeCommentDao.count(wrapper);

            if (userLikeCommentDao.findThumbsUp(userLikeComment)) {// 查找之前是否已经点赞了
                boolean result = userLikeCommentDao.cancelThumbsUp(userLikeComment);
                if (result) {
                    boolean commentResult = commentDao.reduceLikeNumber(pvLikeReq.getId());
                    return commentResult;
                }
                return false;
            } else {
                return false;
            }
        }
    }

    public Map<String, Object> getProjectDetailsList(PvDetailReq req) {
        EpcpvDetailsReq request = new EpcpvDetailsReq();
        EpcpvDetailsCondition condition = new EpcpvDetailsCondition();
        request.setPageNo(req.getPage());
        request.setPageSize(req.getSize());
        Opt.ofBlankAble(req.getZone()).ifPresentOrElse(s -> condition.setRegionName(s.substring(0, s.length() - 1)), () -> condition.setRegionName(""));
        condition.setStageShowName(Opt.ofBlankAble(req.getPeriod()).orElse(""));
        condition.setQueryDateStart(Opt.ofBlankAble(req.getStartdate()).orElse(""));
        condition.setQueryDateEnd(Opt.ofBlankAble(req.getEnddate()).orElse(""));
        request.setCondition(condition);
        return epcpvService.getProjectDetailsList(request);
    }
}
