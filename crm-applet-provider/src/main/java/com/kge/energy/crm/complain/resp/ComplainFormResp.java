package com.kge.energy.crm.complain.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class ComplainFormResp {

    private Integer complainId;

    private Integer userId;

    private Integer typef;

    private String subject;

    private String content;

    private String company;

    private String contacts;

    private String phone;

    private String address;

    private Integer status;

    private String feedback;

    private Integer processUserId;

    private String processTime;

    private Integer flag;

    private Integer createUserId;

    private Integer modifyUserId;

    private String remark;

    private Integer formId;

    private String businessName;

    private String serviceCompany;

    private String createTime;

    private String orderCode;

    private String contractCode;

    private String orderStatus;

    private String contractStatus;

    private String orgName;

    private String realname;

    private List<String> picsPath;

    private String contractName;

}
