package com.kge.energy.crm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
@AllArgsConstructor
public enum ComplainStatusEnums {

    WAITING(0, "待处理"),
    HANDLING(1, "处理中"),
    FINISH(2, "已完成");

    private final Integer code;

    private final String desc;

    public static ComplainStatusEnums getByCode(Integer code) {
        return Arrays.stream(values())
                .filter(e -> Objects.equals(e.getCode(), code))
                .findFirst()
                .orElse(null);
    }

    public static ComplainStatusEnums getByDesc(String desc) {
        return Arrays.stream(values())
                .filter(e -> Objects.equals(e.getDesc(), desc))
                .findFirst()
                .orElse(null);
    }

    public static Integer getCodeByDesc(String desc) {
        return Arrays.stream(values())
                .filter(e -> Objects.equals(e.getDesc(), desc))
                .findFirst().map(ComplainStatusEnums::getCode)
                .orElse(null);
    }

}
