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

    BIZORDER_CREATE_NOTIFY("crm/bizOrder/createNotify", "新增业务工单下发通知"),

    BIZORDER_ASSIGN_NOTIFY("crm/bizOrder/assignNotify", "分派业务工单下发通知"),

    BIZORDER_HANDLE_NOTIFY("crm/bizOrder/handleNotify", "处理业务工单下发通知"),

    BIZORDER_FINISH_NOTIFY("crm/bizOrderfinishNotify", "完成业务工单下发通知"),

    BIZORDER_RETURN_NOTIFY("crm/bizOrder/returnNotify", "退回业务工单下发通知"),

    BIZORDER_WITHDRAW_NOTIFY("crm/bizOrder/withdrawNotify", "撤回业务工单下发通知"),

    BIZORDER_TERMINATE_NOTIFY("crm/bizOrder/terminateNotify", "终止业务工单下发通知"),

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
