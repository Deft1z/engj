package com.kge.energy.crm.common.net;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author wangjihua
 */
@Getter
@AllArgsConstructor
public enum ResponseCode implements Serializable {

    SUC(0, "", "none"),

    PARAM_NOT_VALID(1, "参数验证失败", "message"),

    SHOULD_LOGIN(2, "登录失败", "message"),

    NOT_FOUND(3, "未找到", "message"),

    URL_NOT_FOUND(4, "未找到", "message"),

    PARSE_FAIL(5, "解析错误", "message"),

    DB_FAIL(6, "数据库错误", "messagebox"),

    UUNKNOWN(7, "未知错误", "messagebox"),

    Exist(8, "参数已存在", "message"),

    VIDEO_FAIL(9, "视频错误", "messagebox"),

    DB_UPDATE_FAIL(10, "更新失败", "message"),

    DB_INSERT_FAIL(11, "添加失败", "message"),

    DB_DELETE_FAIL(12, "删除失败", "message"),

    DB_SELETE_FAIL(13, "数据库取数失败", "message"),

    CONV_FAIL(14, "数据类型转换失败", "message"),

    ENCRYPTION_FAIL(15, "数据加解密失败", "message"),

    TOKEN_FAIL(16, "重新登录", "message"),

    FILE_READ_FAIL(17, "文件读取失败", "message"),

    UNAME_REPEATED(18, "用户名重复", "message"),

    ORG_MAX_FAIL(19, "租户已达到系统上限", "message"),

    ZONE_MAX_FAIL(20, "当前租户的园区已达到系统上限", "message"),

    BUILD_MAX_FAIL(21, "当前园区的建筑已达到系统上限", "message"),

    FLOOR_MAX_FAIL(22, "当前建筑的楼层已达到系统上限", "message"),

    ROOM_MAX_FAIL(23, "当前楼层的房间已达到系统上限", "message"),

    AUTHORITY_FAIL(24, "权限不足", "message"),

    COMMUNICATE_FAIL(25, "通讯失败", "message"),

    SCRIPT_FAIL(26, "脚本执行错误", "message"),

    RESP_FAIL(27, "返回值格式错误", "message"),
    ;

    private final Integer code;

    private final String msg;

    private final String showType;
}
