package com.kge.energy.crm.comment.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.kge.energy.crm.comment.req.CmsCommentAddReq;
import com.kge.energy.crm.comment.req.WfFormCommentReq;
import com.kge.energy.crm.comment.resp.CmsCommentResp;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.enums.CmsCommentBizType;
import com.kge.energy.crm.enums.FlagEnums;
import com.kge.energy.crm.repository.dao.CmsCommentDao;
import com.kge.energy.crm.repository.dao.WfFormFlowDao;
import com.kge.energy.crm.repository.entity.BApp;
import com.kge.energy.crm.repository.entity.CmsComment;
import com.kge.energy.crm.repository.entity.WfFormFlow;
import com.kge.energy.crm.repository.entityext.param.CmsCommentParam;
import com.kge.energy.crm.repository.entityext.result.CmsCommentResult;
import com.kge.platform.framework.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CmsCommentService {

    private final CmsCommentDao cmsCommentDao;

    private final WfFormFlowDao wfFormFlowDao;

    public Boolean addComment(CmsCommentAddReq cmsCommentAddReq) {
        CmsComment cmsComment = BeanUtil.copyProperties(cmsCommentAddReq, CmsComment.class);
        cmsComment.setCreateUserId(UserInfoContextUtils.getCurrentUserId());
        cmsComment.setFlag(FlagEnums.NORMAL.getFlag());
        return cmsCommentDao.save(cmsComment);
    }

    public List<CmsCommentResp> getCmsCommentList(CmsCommentParam cmsCommentParam) {
        List<CmsCommentResult> resultList = cmsCommentDao.getCmsCommentList(cmsCommentParam);
        return BeanUtil.copyToList(resultList, CmsCommentResp.class);
    }

    /**
     * 新增工单流程节点评论
     * @param req
     * @return
     */
    public Boolean addWfFormFlowComment(WfFormCommentReq req){
        if(ObjectUtil.isNull(req.getParentCommentId())){
            //如果是新增评论
            //获取当前工单最新节点
            WfFormFlow latestFlow = new LambdaQueryChainWrapper<> (WfFormFlow.class)
                    .eq(WfFormFlow::getFormId, req.getFormId())
                    .orderByDesc(WfFormFlow::getCreateTime)
                    .list().get(0);
            req.setBizType(CmsCommentBizType.ORDER.getCode());
            req.setBizDataId(latestFlow.getFormFlowId());

        } else {
            //如果是回复评论
            //获取父评论
            CmsComment pComment = cmsCommentDao.getById(req.getParentCommentId());
            if(ObjectUtil.isNull(pComment)){
                throw new ServiceException("父评论不存在");
            }

            req.setBizType(pComment.getBizType());
            req.setBizDataId(pComment.getBizDataId());
        }
        return addComment(req);
    }

}
