package com.kge.energy.crm.news.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * @author wangjihua
 */
@Data
@Schema(description = "首页所有渠道新闻列表请求参数")
public class IndexAllChannelNewsReq {

    @Min(value = 1, message = "每页数量不能小于1")
    @Max(value = 10, message = "每页数量不能大于10")
    @Schema(description = "每页数量")
    private Integer pageSize = 1;
}
