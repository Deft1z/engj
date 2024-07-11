package com.kge.energy.crm.common.go;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 转换为Go项目接口格式
 * null 值：字符串 -> ""，数值 -> 0
 *
 * @author wangjihua
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConvertToGoFormats {
}
