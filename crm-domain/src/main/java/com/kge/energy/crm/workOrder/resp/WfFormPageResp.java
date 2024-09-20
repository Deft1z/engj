package com.kge.energy.crm.workOrder.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
public class WfFormPageResp {

    private Integer formId;
    private Integer formTypeId;
    private Integer formMetaId;
    private String content;
    private String status;
    private String subStatus;
    private String timeSubmit;
    private String timeReception;
    private String timeFinished;
    private String modifyTime;
    private Integer currentOrgId;
    private Integer currentRoleId;
    private Integer flag;
    private Integer createUserId;
    private Integer modifyUserId;
    private String remark;
    private String businessName;
    private String code;
    private String customerName;
    private String mobile;
    private String companyName;
    private String electricityCapacity;
    private String voltageLevel;
    private String area;
    private String detailedAddress;
    private String userStatus;
    private String orgName;
    private Integer organizationId;
    private String realname;

}
