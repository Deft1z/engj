package com.kge.energy.crm.common.button.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 业务工单按钮枚举类
 *
 * @author wangjihua
 */
@Getter
@AllArgsConstructor
public enum WorkOrderButtonEnum implements ButtonEnum {

    ASSIGN_WORK_ORDER("assgin_work_order", "分派工单", true, null),
    WITHDRAW_WORK_ORDER("withdraw_work_order", "撤回工单", true, null),
    TERMINATE_WORK_ORDER("terminate_work_order", "终止工单", true, null),
    RETURN_WORK_ORDER("return_work_order", "退回工单", true, null),
    HANDLE_WORK_ORDER("handle_work_order", "处理工单", true, null),
    FINISH_WORK_ORDER("finish_work_order", "完成工单", true, null)

    ;

    private final String code;

    private final String name;

    private final Boolean enabled;

    private final String hint;
}
