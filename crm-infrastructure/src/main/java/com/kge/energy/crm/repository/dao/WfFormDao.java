package com.kge.energy.crm.repository.dao;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.enums.CmsCommentBizTypeEnums;
import com.kge.energy.crm.enums.DataPermissionRangeTypeEnums;
import com.kge.energy.crm.repository.entity.WfForm;
import com.kge.energy.crm.repository.entityext.param.CmsCommentParam;
import com.kge.energy.crm.repository.entityext.param.WorkOrderListParam;
import com.kge.energy.crm.repository.entityext.param.WxUserWorkOrderParam;
import com.kge.energy.crm.repository.entityext.result.CmsCommentResult;
import com.kge.energy.crm.repository.entityext.result.FlowResult;
import com.kge.energy.crm.repository.entityext.result.FormResult;
import com.kge.energy.crm.repository.entityext.result.FormWithdrawReturnResult;
import com.kge.energy.crm.repository.mapper.CmsCommentMapper;
import com.kge.energy.crm.repository.mapper.WfFormMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 表单(WfForm)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class WfFormDao extends ServiceImpl<WfFormMapper, WfForm> {

    private final WfFormMapper mapper;

    private final CmsCommentMapper commentMapper;

    public IPage<FormResult> findList(IPage<WorkOrderListParam> reqIpage, WorkOrderListParam workOrderListParam,
                                      UserInfoDto userInfoDto) {
        return mapper.findList(reqIpage, workOrderListParam, userInfoDto);
    }

    public IPage<FormResult> findWxUserWorkOrder(IPage<WxUserWorkOrderParam> reqIpage, WxUserWorkOrderParam wxUserWorkOrderParam) {
        IPage<FormResult> res = mapper.findWxUserWorkOrder(reqIpage, wxUserWorkOrderParam);
        return res;
    }

    public Long findOrderNum(String startTime, String endTime) {
        return mapper.findOrderNum(startTime, endTime);
    }

    public Long findNewConsultingCount(String startTime, String endTime) {
        return mapper.findNewConsultingCount(startTime, endTime);
    }

    public IPage<FormResult> findListForWx(IPage<WorkOrderListParam> reqIpage, WorkOrderListParam workOrderListParam,
                                           UserInfoDto userInfoDto, DataPermissionRangeTypeEnums dataEnums) {
        return mapper.findListForWx(reqIpage, workOrderListParam, userInfoDto, dataEnums);
    }

    public List<FormResult> findAll(WorkOrderListParam workOrderListParam,
                                    UserInfoDto userInfoDto, DataPermissionRangeTypeEnums dataEnums) {
        return mapper.findAll(workOrderListParam, userInfoDto, dataEnums);
    }

    public IPage<FormWithdrawReturnResult> findWithdrawReturnList(IPage<WorkOrderListParam> reqIpage, WorkOrderListParam listParam,
                                                                  UserInfoDto userInfoDto, DataPermissionRangeTypeEnums dataEnums) {
        return mapper.findWithdrawReturnList(reqIpage, listParam, userInfoDto, dataEnums);
    }

    public List<FlowResult> getFlowByFormId(Integer formId, UserInfoDto userInfoDto) {
        List<FlowResult> list = mapper.getFlowByFormId(formId, userInfoDto);
        for(FlowResult flowResult : list){
            CmsCommentParam param = new CmsCommentParam(flowResult.getFormFlowId(), CmsCommentBizTypeEnums.ORDER.getCode());
            List<CmsCommentResult> commentResultList = commentMapper.getCmsCommentList(param);

            // 存储父评论的列表
            List<CmsCommentResult> parentComments = new ArrayList<>();

            //存储子评论的列表
            List<CmsCommentResult> childrenComments = new ArrayList<>();

            // 用于快速查找根评论的映射
            Map<Integer, CmsCommentResult> rootCommentMap = new HashMap<>();

            //用于快速查找子评论的根评论映射
            Map<Integer, Integer> childrenParentMap = new HashMap<>();

            //分离父评论和子评论
            for (CmsCommentResult comment : commentResultList) {
                if (comment.getParentCommentId() == null) {
                    parentComments.add(comment);
                    rootCommentMap.put(comment.getCommentId(), comment);
                } else {
                    childrenComments.add(comment);
                    childrenParentMap.put(comment.getCommentId(), findRootComment(commentResultList, comment).getCommentId());
                }
            }

            for(CmsCommentResult childrenCommentResult : childrenComments) {
                Integer rootCommentId = childrenParentMap.get(childrenCommentResult.getCommentId());
                CmsCommentResult rootCommentResult = rootCommentMap.get(rootCommentId);
                if(CollUtil.isEmpty(rootCommentResult.getChildrenCommentList())){
                    rootCommentResult.setChildrenCommentList(new ArrayList<>());
                }
                // 将子评论转换为 ChildrenCommentResult 对象
                CmsCommentResult.ChildrenCommentResult childComment = new CmsCommentResult.ChildrenCommentResult()
                        .setCommentId(childrenCommentResult.getCommentId())
                        .setParentCommentId(childrenCommentResult.getParentCommentId())
                        .setName(childrenCommentResult.getName())
                        .setReplyName(childrenCommentResult.getReplyName())
                        .setContent(childrenCommentResult.getContent())
                        .setDate(childrenCommentResult.getDate());

                rootCommentResult.getChildrenCommentList().add(childComment);
            }


            flowResult.setCommentList(parentComments);
        }
        return list;
    }

    private CmsCommentResult findRootComment(List<CmsCommentResult> allComments, CmsCommentResult targetComment) {
        // 用于快速查找记录的映射
        Map<Integer, CmsCommentResult> recordMap = new HashMap<>();
        for(CmsCommentResult comment : allComments){
            recordMap.put(comment.getCommentId(), comment);
        }
        return findRootCommentRecursive(recordMap, targetComment);
    }

    private static CmsCommentResult findRootCommentRecursive(Map<Integer, CmsCommentResult> recordMap, CmsCommentResult currentRecord) {
        if (currentRecord.getParentCommentId() == null) {
            return currentRecord; // 找到根节点
        }

        // 获取当前记录的父记录
        CmsCommentResult parentRecord = recordMap.get(currentRecord.getParentCommentId());
        if (parentRecord == null) {
            throw new IllegalArgumentException("Parent record not found");
        }

        // 递归查找父记录的根节点
        return findRootCommentRecursive(recordMap, parentRecord);
    }

}

