package com.kge.energy.crm.common.page;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.util.Map;

/**
 * @author wangjihua
 */
@Data
public class PageReq {

    @Min(value = 1, message = "当前页数不能小于1")
    private Long currentPage = 1L;

    @Min(value = 1, message = "每页数量不能小于1")
    @Max(value = 100, message = "每页数量不能大于100")
    private Long pageSize = 10L;

    private Map<String, String> sort;
}
