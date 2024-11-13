package com.kge.energy.crm.repository.entityext.result.news;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
@Schema(description = "新闻列表响应体")
public class NewsDetailResult {

    @Schema(description = "新闻id")
    private Integer id;

    @Schema(description = "新闻标题")
    private String title;

    @Schema(description = "新闻内容")
    private String content;

    @Schema(description = "新闻附件")
    private List<Attachment> attachments;

    @Schema(description = "新闻发布日期")
    private LocalDateTime publishDate;

    @Data
    public static class Attachment {

        @Schema(description = "附件名称")
        private String name;

        @Schema(description = "附件地址")
        private String filepath;
    }

}
