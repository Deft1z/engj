package com.kge.energy.crm.easyexcel;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
@AllArgsConstructor
public enum DictEnum {

    COMPLAIN_TYPE_1("complain-type", "1", "工单投诉"),
    COMPLAIN_TYPE_2("complain-type", "2", "合同投诉"),
    COMPLAIN_STATUS_0("complain-status", "0", "待处理"),
    COMPLAIN_STATUS_1("complain-status", "1", "处理中"),
    COMPLAIN_STATUS_2("complain-status", "2", "已解决"),
    MSG_BIZ_TYPE_0("msg-biz-type", "0", "告警通知"),
    MSG_BIZ_TYPE_1("msg-biz-type", "1", "工单通知"),
    MSG_BIZ_TYPE_2("msg-biz-type", "2", "项目合同"),
    MSG_BIZ_TYPE_3("msg-biz-type", "3", "投诉处理"),
    MSG_BIZ_TYPE_4("msg-biz-type", "4", "登录提醒"),
    ;

    final String type;

    final String value;

    final String label;

    public static String getLabel(String type, String value) {
        DictEnum dictEnum = Arrays.stream(values())
                .filter(e -> Objects.equals(e.getType(), type) && Objects.equals(e.getValue(), value))
                .findFirst()
                .orElse(null);
        return dictEnum == null ? "" : dictEnum.getLabel();
    }

    public static String getValue(String type, String label) {
        DictEnum dictEnum = Arrays.stream(values())
                .filter(e -> Objects.equals(e.getType(), type) && Objects.equals(e.getLabel(), label))
                .findFirst()
                .orElse(null);
        return dictEnum == null ? "" : dictEnum.getValue();
    }

}