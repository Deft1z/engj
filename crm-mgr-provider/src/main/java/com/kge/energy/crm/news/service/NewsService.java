package com.kge.energy.crm.news.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import com.kge.energy.crm.news.req.UploadNewsReq;
import com.kge.energy.crm.repository.dao.BNewsRecordDao;
import com.kge.energy.crm.repository.dao.BNewsTypeDao;
import com.kge.energy.crm.repository.entity.BNewsRecord;
import com.kge.energy.crm.repository.entity.BNewsType;
import com.kge.platform.framework.web.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wangjihua
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NewsService {

    private final BNewsTypeDao bNewsTypeDao;

    private final BNewsRecordDao bNewsRecordDao;

    /**
     * 上传新闻
     */
    public Boolean uploadNews(UploadNewsReq req) {

        if (CollectionUtil.isEmpty(req.getNewsList())) {
            return true;
        }

        BNewsType bNewsType = bNewsTypeDao.selectOneByCode(req.getChannelCode(), req.getTypeCode());
        Assert.notNull(bNewsType, "该新闻渠道类型不存在");

        List<BNewsRecord> bNewsRecords = req.getNewsList().stream()
                .map(news -> new BNewsRecord()
                        .setTypeId(bNewsType.getId())
                        .setTitle(news.getTitle())
                        .setContent(news.getContent())
                        .setNumber(news.getNumber())
                        .setAttachment(CollectionUtil.isEmpty(news.getAttachments()) ? null : JsonUtils.serialize(news.getAttachments())).setPublishDate(news.getPublishDate())
                        .setSourceUrl(news.getSourceUrl())
                ).toList();

        bNewsRecordDao.saveUploadNews(bNewsRecords);

        return true;
    }
}
