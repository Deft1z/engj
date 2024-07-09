package com.kge.energy.crm.common.page;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
public class PageResp<T> {

    private Long currentPage;

    private Long pageSize;

    private Long total;

    private List<T> list;
}
