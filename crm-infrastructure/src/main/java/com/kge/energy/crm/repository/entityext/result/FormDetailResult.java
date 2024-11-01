package com.kge.energy.crm.repository.entityext.result;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class FormDetailResult {
    private Integer formId;
    private String businessName;
    private String timeSubmit;
    private String code;
    private String subStatus;
    private String orgName;
    private String realname;
    private String remark;
}
