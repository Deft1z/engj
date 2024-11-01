package com.kge.energy.crm.common.util;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberUtils {

    public static boolean isPositiveInteger(String str) {
        // 定义正则表达式
        String regex = "^[1-9]\\d*$";
        // 编译正则表达式
        Pattern pattern = Pattern.compile(regex);
        // 创建 matcher 对象
        Matcher matcher = pattern.matcher(str);
        // 检查字符串是否匹配正则表达式
        return matcher.matches();
    }

}
