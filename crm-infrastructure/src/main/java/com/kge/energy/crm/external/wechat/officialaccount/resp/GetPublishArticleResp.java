package com.kge.energy.crm.external.wechat.officialaccount.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
public class GetPublishArticleResp {

    @JsonProperty("total_count")
    private String totalCount;

    @JsonProperty("item_count")
    private String itemCount;

    private List<ItemBean> item;

    @Data
    public static class ItemBean {

        @JsonProperty("article_id")
        private String articleId;

        private ContentBean content;

        @JsonProperty("update_time")
        private String updateTime;

        @Data
        public static class ContentBean {

            @JsonProperty("news_item")
            private List<NewsItemBean> newsItem;

            @Data
            public static class NewsItemBean {

                private String title;

                private String author;

                private String digest;

                private String content;

                @JsonProperty("content_source_url")
                private String contentSourceUrl;

                @JsonProperty("thumb_media_id")
                private String thumbMediaId;

                @JsonProperty("show_cover_pic")
                private int showCoverPic;

                @JsonProperty("need_open_comment")
                private int needOpenComment;

                @JsonProperty("only_fans_can_comment")
                private int onlyFansCanComment;

                private String url;

                @JsonProperty("is_deleted")
                private boolean isDeleted;
            }
        }
    }
}
