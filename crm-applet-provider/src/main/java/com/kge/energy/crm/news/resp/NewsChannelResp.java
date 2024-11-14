package com.kge.energy.crm.news.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
@Schema(description = "新闻渠道响应体")
public class NewsChannelResp {

    @Schema(description = "所有渠道")
    private List<NewsChannel> newsChannels;

    @Data
    @Accessors(chain = true)
    public static class NewsChannel {

        @Schema(description = "渠道id")
        private Integer channelId;

        @Schema(description = "渠道名称")
        private String channelName;

        @Schema(description = "渠道编码")
        private String channelCode;

        @Schema(description = "新闻类型")
        private List<NewsType> newsTypes;

    }


    @Data
    @Accessors(chain = true)
    public static class NewsType {

        @Schema(description = "新闻类型ID")
        private Integer typeId;

        @Schema(description = "新闻类型名称")
        private String typeName;

        @Schema(description = "新闻类型编码")
        private String typeCode;

    }
}
