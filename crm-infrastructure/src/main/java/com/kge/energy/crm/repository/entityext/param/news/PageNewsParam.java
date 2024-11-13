package com.kge.energy.crm.repository.entityext.param.news;

import com.kge.energy.crm.common.page.PageReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wangjihua
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PageNewsParam extends PageReq {

    private String searchKeyword;

    private Integer typeId;
}
