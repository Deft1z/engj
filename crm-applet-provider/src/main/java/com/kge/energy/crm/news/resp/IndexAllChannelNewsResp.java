package com.kge.energy.crm.news.resp;

import com.kge.energy.crm.repository.entityext.result.news.NewsListResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
@Schema(description = "首页所有渠道新闻列表响应参数")
public class IndexAllChannelNewsResp {

    @Schema(description = "所有渠道新闻")
    private List<ChannelNews> channelNewsList;

    @Data
    @Accessors(chain = true)
    public static class ChannelNews {

        @Schema(description = "新闻类型ID")
        private Integer typeId;

        @Schema(description = "新闻列表")
        private List<NewsListResult> newsList;

    }

}
