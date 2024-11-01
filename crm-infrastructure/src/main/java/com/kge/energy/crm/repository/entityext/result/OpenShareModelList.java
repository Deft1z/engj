package com.kge.energy.crm.repository.entityext.result;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wangrongjun
 */
@Data
@Accessors(chain = true)
public class OpenShareModelList {

    private Integer uid;

    private String realname;

    private String mobile;
}
