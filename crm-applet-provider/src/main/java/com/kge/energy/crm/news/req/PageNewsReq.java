package com.kge.energy.crm.news.req;

import com.kge.energy.crm.common.page.PageReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wangjihua
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "分页渠道新闻列表")
public class PageNewsReq extends PageReq {

    @Schema(description = "搜索关键字")
    private String searchKeyword;

    @Schema(description = "新闻类型ID")
    private Integer typeId;
}
