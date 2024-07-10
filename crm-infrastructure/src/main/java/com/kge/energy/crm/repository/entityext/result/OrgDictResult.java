package com.kge.energy.crm.repository.entityext.result;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
public class OrgDictResult {

    private String label;

    private Integer value;

}
