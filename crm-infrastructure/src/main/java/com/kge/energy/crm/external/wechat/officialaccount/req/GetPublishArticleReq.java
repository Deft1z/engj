package com.kge.energy.crm.external.wechat.officialaccount.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author wangjihua
 */
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class GetPublishArticleReq {

    /**
     * 从全部素材的该偏移位置开始返回，0表示从第一个素材返回
     */
    private Integer offset = 0;

    /**
     * 返回素材的数量，取值在1到20之间
     */
    private Integer count = 5;

    /**
     * 1 表示不返回 content 字段，0 表示正常返回，默认为 0
     */
    @JsonProperty("no_content")
    private Integer noContent = 1;
}
