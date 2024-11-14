package com.kge.energy.crm.news.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.enums.RoleEnums;
import com.kge.energy.crm.news.req.IndexAllChannelNewsReq;
import com.kge.energy.crm.news.req.PageNewsReq;
import com.kge.energy.crm.news.resp.IndexAllChannelNewsResp;
import com.kge.energy.crm.news.resp.NewsChannelResp;
import com.kge.energy.crm.repository.dao.BNewsChannelDao;
import com.kge.energy.crm.repository.dao.BNewsRecordDao;
import com.kge.energy.crm.repository.entity.BNewsRecord;
import com.kge.energy.crm.repository.entityext.param.news.PageNewsParam;
import com.kge.energy.crm.repository.entityext.result.news.NewsChannelResult;
import com.kge.energy.crm.repository.entityext.result.news.NewsDetailResult;
import com.kge.energy.crm.repository.entityext.result.news.NewsListResult;
import com.kge.platform.framework.web.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangjihua
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NewsService {

    private final BNewsChannelDao bNewsChannelDao;

    private final BNewsRecordDao bNewsRecordDao;


    /**
     * 新闻渠道类型
     */
    public NewsChannelResp channels() {

        List<NewsChannelResult> channelResults = bNewsChannelDao.channels();
        if (CollectionUtil.isEmpty(channelResults)) {
            return null;
        }

        // 根据 channelId 分组
        Map<Integer, List<NewsChannelResult>> groupedByChannelId = channelResults.stream()
                .collect(Collectors.groupingBy(NewsChannelResult::getChannelId));

        List<NewsChannelResp.NewsChannel> newsChannels = groupedByChannelId.entrySet().stream()
                .map(entry -> {
                    NewsChannelResp.NewsChannel channel = new NewsChannelResp.NewsChannel()
                            .setChannelId(entry.getKey())
                            .setChannelName(entry.getValue().get(0).getChannelName())
                            .setChannelCode(entry.getValue().get(0).getChannelCode());

                    List<NewsChannelResp.NewsType> newsTypes = entry.getValue().stream()
                            .map(newsType -> new NewsChannelResp.NewsType()
                                    .setTypeId(newsType.getTypeId())
                                    .setTypeName(newsType.getTypeName())
                                    .setTypeCode(newsType.getTypeCode())
                            )
                            .collect(Collectors.toList());

                    channel.setNewsTypes(newsTypes);
                    return channel;
                })
                .collect(Collectors.toList());

        return new NewsChannelResp()
                .setNewsChannels(newsChannels);

    }

    /**
     * 首页所有渠道新闻列表
     */
    public IndexAllChannelNewsResp indexAllChannelNews(IndexAllChannelNewsReq req) {

        List<BNewsRecord> bNewsRecords = bNewsRecordDao.indexAllChannelNews(req.getPageSize());
        if (CollectionUtil.isEmpty(bNewsRecords)) {
            return null;
        }

        // 根据 typeId 分组
        Map<Integer, List<BNewsRecord>> groupedByTypeId = bNewsRecords.stream()
                .collect(Collectors.groupingBy(BNewsRecord::getTypeId));

        List<IndexAllChannelNewsResp.ChannelNews> channelNewsList = groupedByTypeId.entrySet().stream()
                .map(entry -> {
                    IndexAllChannelNewsResp.ChannelNews channelNews = new IndexAllChannelNewsResp.ChannelNews()
                            .setTypeId(entry.getKey());

                    List<NewsListResult> newsList = entry.getValue().stream()
                            .map(bNewsRecord -> new NewsListResult()
                                    .setId(bNewsRecord.getId())
                                    .setTitle(bNewsRecord.getTitle())
                                    .setPublishDate(bNewsRecord.getPublishDate()))
                            .collect(Collectors.toList());
                    channelNews.setNewsList(newsList);

                    return channelNews;
                }).collect(Collectors.toList());

        return new IndexAllChannelNewsResp()
                .setChannelNewsList(channelNewsList);
    }

    public PageResp<NewsListResult> pageNews(PageNewsReq req) {

        PageNewsParam param = BeanUtil.copyProperties(req, PageNewsParam.class);

        IPage<BNewsRecord> pages = bNewsRecordDao.pageNews(param);

        List<NewsListResult> newsListResults = pages.getRecords().stream()
                .map(bNewsRecord -> new NewsListResult()
                        .setId(bNewsRecord.getId())
                        .setTitle(bNewsRecord.getTitle())
                        .setPublishDate(bNewsRecord.getPublishDate()))
                .collect(Collectors.toList());

        return new PageResp<NewsListResult>()
                .setList(newsListResults)
                .setCurrentPage(pages.getCurrent())
                .setPageSize(pages.getSize())
                .setTotal(pages.getTotal());

    }

    /**
     * 新闻详情
     */
    public NewsDetailResult newsDetail(Integer newsId) {

        BNewsRecord bNewsRecord = bNewsRecordDao.getById(newsId);


        return Optional.ofNullable(bNewsRecord)
                .map(record -> new NewsDetailResult()
                        .setId(record.getId())
                        .setTitle(record.getTitle())
                        .setNumber(record.getNumber())
                        .setContent(record.getContent())
                        .setPublishDate(record.getPublishDate())
                        .setAttachments(
                                Optional.ofNullable(record.getAttachment())
                                        .filter(StrUtil::isNotBlank)
                                        .map(attachment -> JsonUtils.deserialize(attachment, new TypeReference<List<NewsDetailResult.Attachment>>() {
                                        }))
                                        .orElse(Collections.emptyList())
                        )
                        .setSourceUrl(record.getSourceUrl())
                ).orElse(null);
    }

    /**
     * 删除新闻
     */
    public boolean delete(Integer newsId) {

        Set<String> roleCodes = UserInfoContextUtils.getCurrentUserInfo().getRoleCodes();

        Set<RoleEnums> CAN_DELETE_NEWS_ROLES = Set.of(
                RoleEnums.SUPER_ADMIN, RoleEnums.TENANT_ADMIN, RoleEnums.OPERATE_ADMIN
        );

        boolean match = CAN_DELETE_NEWS_ROLES.stream()
                .anyMatch(roleEnum -> roleCodes.contains(roleEnum.getCode()));

        if (!match) {
            return false;
        }

        return bNewsRecordDao.removeById(newsId);
    }
}
