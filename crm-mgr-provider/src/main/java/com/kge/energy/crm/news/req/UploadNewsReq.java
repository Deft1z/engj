package com.kge.energy.crm.news.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wangjihua
 */
@Data
@Schema(description = "上传新闻请求体")
public class UploadNewsReq {

    @Schema(description = "渠道编码")
    @NotBlank
    private String channelCode;

    @Schema(description = "类型编码")
    @NotBlank
    private String typeCode;

    @Schema(description = "新闻列表")
    private List<News> newsList;

    @Data
    public static class News {

        @Schema(description = "新闻标题")
        private String title;

        @Schema(description = "新闻编号")
        private String number;

        @Schema(description = "新闻内容")
        private String content;

        @Schema(description = "新闻附件")
        private List<Attachment> attachments;

        @Schema(description = "新闻发布日期")
        private LocalDateTime publishDate;

        @Schema(description = "新闻源地址")
        private String sourceUrl;
    }


    @Data
    public static class Attachment {

        @Schema(description = "附件名称")
        private String name;

        @Schema(description = "附件地址")
        private String filepath;
    }
}
