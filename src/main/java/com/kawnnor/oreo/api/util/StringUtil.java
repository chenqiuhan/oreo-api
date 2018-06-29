package com.kawnnor.oreo.api.util;

public class StringUtil{
    /**
     * 截取字符串str中出现字符symbol后的字符串
     * 例：subStr("21:20",":");返回结果为20
     */
    public static String subStr(String str,String symbol){
        return str.substring(str.indexOf(symbol)+1, str.length());
    }
}