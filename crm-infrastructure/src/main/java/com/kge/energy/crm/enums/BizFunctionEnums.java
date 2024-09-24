package com.kge.energy.crm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * 业务功能枚举类
 *
 * @author wangjihua
 */
@Getter
@AllArgsConstructor
public enum BizFunctionEnums {

    BIZORDER_LIST("crm/bizOrder/list", "查看业务工单列表"),

    BIZORDER_CREATE("crm/bizOrder/create", "新增业务工单"),

    BIZORDER_ASSIGN("crm/bizOrder/assign", "分派业务工单"),

    BIZORDER_RETURN("crm/bizOrder/return", "退回业务工单"),

    BIZORDER_WITHDRAW("crm/bizOrder/withdraw", "撤回业务工单"),

    BIZORDER_TERMINATE("crm/bizOrder/terminate", "终止业务工单"),

    CONTRACT_LIST("crm/contract/list", "查看服务合同列表"),

    COMPLAIN_LIST("crm/complain/list", "查看投诉建议列表"),

    ECC_OM_REPORT_LIST("crm/ecc/omReport/list", "查看运维报告列表"),

    ;


    private final String code;

    private final String name;

    public static BizFunctionEnums getByCode(Integer code) {
        return Arrays.stream(values())
                .filter(e -> Objects.equals(e.getCode(), code))
                .findFirst()
                .orElse(null);
    }
}
