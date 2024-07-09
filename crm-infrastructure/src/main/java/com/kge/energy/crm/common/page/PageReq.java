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

    @Min(1)
    private Long currentPage = 1L;

    @Min(1)
    @Max(100)
    private Long pageSize = 10L;

    private Map<String, String> sort;
}
