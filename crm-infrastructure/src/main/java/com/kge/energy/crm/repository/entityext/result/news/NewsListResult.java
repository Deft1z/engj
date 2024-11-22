package com.kge.energy.crm.repository.entityext.result.news;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
@Schema(description = "新闻列表响应体")
public class NewsListResult {

    @Schema(description = "新闻id")
    private Integer id;

    @Schema(description = "新闻标题")
    private String title;

    @Schema(description = "新闻发布日期")
    private LocalDateTime publishDate;

}
