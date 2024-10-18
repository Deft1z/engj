package com.kge.energy.crm.repository.entityext.result;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class FormWithdrawReturnResult {
    private Integer formId;
    private String businessName;
    private String code;
    private String timeSubmit;
    private String modifyTime;
    private String orgName;
    private String actionContent;
    private String status;
}
