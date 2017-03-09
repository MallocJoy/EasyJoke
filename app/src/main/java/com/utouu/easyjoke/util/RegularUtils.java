package com.utouu.easyjoke.util;


import com.utouu.android.commons.utils.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cn.utsoft.xunions.util.ConstUtils.*;
import static cn.utsoft.xunions.util.ConstUtils.IS_NUMBER;
import static cn.utsoft.xunions.util.ConstUtils.REGEX_CHZ;
import static cn.utsoft.xunions.util.ConstUtils.REGEX_DATE;
import static cn.utsoft.xunions.util.ConstUtils.REGEX_EMAIL;
import static cn.utsoft.xunions.util.ConstUtils.REGEX_IDCARD15;
import static cn.utsoft.xunions.util.ConstUtils.REGEX_IDCARD18;
import static cn.utsoft.xunions.util.ConstUtils.REGEX_IP;
import static cn.utsoft.xunions.util.ConstUtils.REGEX_MOBILE_EXACT;
import static cn.utsoft.xunions.util.ConstUtils.REGEX_MOBILE_SIMPLE;
import static cn.utsoft.xunions.util.ConstUtils.REGEX_TEL;
import static cn.utsoft.xunions.util.ConstUtils.REGEX_TESHU;
import static cn.utsoft.xunions.util.ConstUtils.REGEX_URL;
import static cn.utsoft.xunions.util.ConstUtils.REGEX_USERNAME;


/**
 * 验证手机号（简单） isMobileSimple
 * 验证手机号（精确） isMobileExact
 * 验证电话号码 isTel
 * 验证身份证号码15位 isIDCard15
 * 验证身份证号码18位 isIDCard18
 * 验证邮箱 isEmail
 * 验证URL isURL
 * 验证汉字 isChz
 * 验证用户名 isUsername
 * 验证yyyy-MM-dd格式的日期校验，已考虑平闰年 isDate
 * 验证IP地址 isIP
 * string是否匹配regex isMatch
 * Created by Du_Li on 2016/9/8.
 */
public class RegularUtils {
    private RegularUtils() {
        throw new UnsupportedOperationException("********----------********");
    }

    /**
     * If u want more please visit http://toutiao.com/i6231678548520731137/
     */

    /**
     * 验证手机号（简单）
     *
     * @param string 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isMobileSimple(String string) {
        return isMatch(REGEX_MOBILE_SIMPLE, string);
    }

    /**
     * 验证手机号（精确）
     *
     * @param string 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isMobileExact(String string) {
        return isMatch(REGEX_MOBILE_EXACT, string);
    }

    /**
     * 验证电话号码
     *
     * @param string 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isTel(String string) {
        return isMatch(REGEX_TEL, string);
    }

    /**
     * 验证身份证号码15位
     *
     * @param string 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isIDCard15(String string) {
        return isMatch(REGEX_IDCARD15, string);
    }

    /**
     * 验证身份证号码18位
     *
     * @param string 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isIDCard18(String string) {
        return isMatch(REGEX_IDCARD18, string);
    }

    /**
     * 验证邮箱
     *
     * @param string 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isEmail(String string) {
        return isMatch(REGEX_EMAIL, string);
    }

    /**
     * 验证URL
     *
     * @param string 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isURL(String string) {
        return isMatch(REGEX_URL, string);
    }

    /**
     * 验证汉字
     *
     * @param string 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isChz(String string) {
        return isMatch(REGEX_CHZ, string);
    }

    /**
     * 验证用户名
     * <p>取值范围为a-z,A-Z,0-9,"_",汉字，不能以"_"结尾,用户名必须是6-20位</p>
     *
     * @param string 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isUsername(String string) {
        return isMatch(REGEX_USERNAME, string);
    }

    /**
     * 验证yyyy-MM-dd格式的日期校验，已考虑平闰年
     *
     * @param string 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isDate(String string) {
        return isMatch(REGEX_DATE, string);
    }

    /**
     * 验证IP地址
     *
     * @param string 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isIP(String string) {
        return isMatch(REGEX_IP, string);
    }

    /**
     * 验证是否包含特殊字符
     *
     * @param string 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isContainF(String string) {
        Pattern p = Pattern.compile(REGEX_TESHU);
        Matcher matcher = p.matcher(string);
        if (matcher.find()) {
            return true;
        }
        return false;
    }

    public static boolean isNumber(String string) {
        Pattern p = Pattern.compile(IS_NUMBER);
        Matcher matcher = p.matcher(string);
        if (matcher.find()) {
            return true;
        } else
            return false;
    }

    /**
     * string是否匹配regex
     *
     * @param regex  正则表达式字符串
     * @param string 要匹配的字符串
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isMatch(String regex, String string) {
        return !StringUtils.isEmpty(string) && Pattern.matches(regex, string);
    }


    /**
     * 校验密码格式 (6~18位且不能有汉字或空格)
     *
     * @param password
     * @return
     */
    public static boolean checkPass(String password) {
        return Pattern.compile("^[^\\u4e00-\\u9fa5^ ]{6,18}$").matcher(password).matches();
    }

}