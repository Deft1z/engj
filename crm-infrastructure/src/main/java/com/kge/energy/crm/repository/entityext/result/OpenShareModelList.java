package com.kge.energy.crm.repository.entityext.result;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wangrongjun
 */
@Data
@Accessors(chain = true)
public class OpenShareModelList {
    Integer Uid;
    String  Realname;
    String  Mobile;
}
