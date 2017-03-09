package com.utouu.easyjoke.util;

import java.text.NumberFormat;

/**
 * Created: AriesHoo on 2017-03-06 20:56
 * Function: 格式化double
 * Desc:
 */
public class NumberFormatUtil {

    public static String formatNumber(double format) {
        String str = format + "";
        try {
            NumberFormat currency = NumberFormat.getNumberInstance();
            currency.setMinimumFractionDigits(2);//设置数的小数部分所允许的最小位数(如果不足后面补0)
            currency.setMaximumFractionDigits(4);//设置数的小数部分所允许的最大位数(如果超过会四舍五入)
            str = currency.format(format);//12,343,171.60
        } catch (Exception e) {

        }
        return str;
    }
}
