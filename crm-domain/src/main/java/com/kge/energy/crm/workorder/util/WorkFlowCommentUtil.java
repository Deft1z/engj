package com.kge.energy.crm.workorder.util;

import com.kge.energy.crm.repository.entityext.result.CmsCommentResult;
import com.kge.energy.crm.repository.entityext.result.FlowResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkFlowCommentUtil {

    public static void handleWorkFlowComment(List<FlowResult> flowList) {
        for(FlowResult flowResult : flowList){
            List<CmsCommentResult> commentResultList = flowResult.getCommentList();

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
    }

    private static CmsCommentResult findRootComment(List<CmsCommentResult> allComments, CmsCommentResult targetComment) {
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
