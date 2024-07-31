package com.kge.energy.crm.pv.service;

import cn.hutool.core.bean.BeanUtil;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.external.epcpv.req.EpcpvInfoReq;
import com.kge.energy.crm.external.epcpv.service.EpcpvService;
import com.kge.energy.crm.pv.req.PvInfoReq;
import com.kge.energy.crm.external.epcpv.property.EpcpvProperties;
import com.kge.energy.crm.pv.resp.AppletCommentResp;
import com.kge.energy.crm.repository.dao.CmsCommentDao;
import com.kge.energy.crm.repository.entityext.result.AppletCommentResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PvService {

    private final EpcpvService epcpvService;

    private final CmsCommentDao commentDao;

    public Map<String, Object> getAllPvInfo(PvInfoReq pvInfoReq){
        EpcpvInfoReq epcpvInfoReq = new EpcpvInfoReq(pvInfoReq.getStartdate(), pvInfoReq.getEnddate());
        Map<String, Object> resultMap = epcpvService.getAllPvInfo(epcpvInfoReq);

        //获取评论列表
        List<AppletCommentResp> comments = new ArrayList<>();
        Integer currentUserId = UserInfoContextUtils.getCurrentUserId();
        List<AppletCommentResult> commentList =commentDao.getAllCommentsByUserId(currentUserId);
        for(AppletCommentResult appletCommentResult : commentList){
            AppletCommentResp appletCommentResp = new AppletCommentResp();
            BeanUtil.copyProperties(appletCommentResult, appletCommentResp);
            appletCommentResp.setRealname(appletCommentResult.getName());

            if(appletCommentResult.getCalLikeNum() != appletCommentResult.getThumb()){
                boolean updateResult = commentDao.updateLikeNumber(appletCommentResult.getId(), appletCommentResult.getCalLikeNum());
                if(updateResult){
                    appletCommentResp.setThumb(appletCommentResult.getCalLikeNum());
                }
            }
            comments.add(appletCommentResp);
        }
        resultMap.put("comments", comments);

        return resultMap;
    }


}
