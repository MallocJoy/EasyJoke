package com.utouu.easyjoke.util;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.widget.TextView;

import static android.R.attr.format;

/**
 * Created: AriesHoo on 2017-02-19 10:29
 * Function: 格式化工具类--TextView改变部分字体大小,String 格式化输出
 * Desc:
 */
public class FormatUtil {


    public static String formatCommaString(Object o) {
        return formatCommaString(o, false, true);
    }

    /**
     * 将数字格式化添加,分割并保留length位小数
     *
     * @param o      需要格式化对象 Double/Float/Integer
     * @param isWrap 是否换行
     * @param isDot  是否保留小数
     * @return
     */
    public static String formatCommaString(Object o, boolean isWrap, boolean isDot) {
        LoggerUtil.e("format:" + format);
        if (!(o instanceof Double) && !(o instanceof Float) && !(o instanceof Integer) && !(o instanceof Long)) {
            return o != null ? o.toString() : "";
        }
        String end = "f";
        if (o instanceof Integer || o instanceof Long) {
            end = "d";
        }
        return String.format("%,3" + (isDot && (!(o instanceof Integer || o instanceof Long)) ? ".2" + end : end) + (isWrap ? "%n" : ""), o);
    }

    /**
     * 格式化文本中百分比号--%号改变大小
     *
     * @param tv
     * @param textSize
     * @return
     */
    public static boolean formatTextSizePercent(TextView tv, int textSize) {
        if (tv == null) {
            return false;
        }
        String result = tv.getText().toString();
        int index = result.lastIndexOf(".");
        if (index != -1) {
            return formatTextSizeDot(tv, textSize);
        } else {
            index = result.indexOf("%");
        }
        return formatTextSize(tv, index, result.length(), textSize, true);
    }

    /**
     * 格式化小数点--.及以后文本变化大小
     *
     * @param tv
     * @param textSize
     * @return
     */
    public static boolean formatTextSizeDot(TextView tv, int textSize) {
        if (tv == null) {
            return false;
        }
        String result = tv.getText().toString();
        int index = result.lastIndexOf(".");
        return formatTextSize(tv, index, result.length(), textSize, true);
    }

    /**
     * 格式化文本
     *
     * @param tv       TextView对象
     * @param start    格式化起点
     * @param end      格式化终点
     * @param textSize 格式化的文字大小
     * @param isDip    是否为dp单位
     * @return
     */
    public static boolean formatTextSize(TextView tv, int start, int end, int textSize, boolean isDip) {
        if (tv == null) {
            return false;
        }
        String result = tv.getText().toString();
        if (start < 0 || start > result.length() - 1 || start >= end || end > result.length()) {
            return false;
        }
        SpannableString spanText = new SpannableString(result);
        AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(textSize, isDip);
        spanText.setSpan(sizeSpan, start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        tv.setText(spanText);
        return true;
    }
}
