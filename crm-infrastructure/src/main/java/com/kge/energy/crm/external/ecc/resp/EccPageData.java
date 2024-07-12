package com.kge.energy.crm.external.ecc.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class EccPageData<T> {
    /**
     * 页码
     */
    private Integer pageNum;

    /**
     * 分页大小
     */
    private Integer pageSize;

    /**
     * 页数
     */
    private Integer size;

    /**
     * 总数
     */
    private Integer total;

    /**
     * 内容
     */
    private List<T> list;
}
