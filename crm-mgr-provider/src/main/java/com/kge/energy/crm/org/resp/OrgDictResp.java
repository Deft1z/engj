package com.kge.energy.crm.org.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
public class OrgDictResp {

    private String label;

    private Integer value;

}
