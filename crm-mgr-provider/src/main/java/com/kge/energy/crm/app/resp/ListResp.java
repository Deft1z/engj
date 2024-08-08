package com.kge.energy.crm.app.resp;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * wangrongjun
 */
@NoArgsConstructor
@Data
public class ListResp {

    /**
     * 总数
     */
    private Integer total;

    /**
     * 条目
     */
    private List<ListContent> content;

}
