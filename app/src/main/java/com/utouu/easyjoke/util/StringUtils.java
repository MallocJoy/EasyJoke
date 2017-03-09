package com.utouu.easyjoke.util;

import java.io.UnsupportedEncodingException;

/**
 * Created by llh on 2017/2/28 11:23.
 * Function:
 * Desc:
 */

public class StringUtils {


    /**
     * 字符串中英文混合根据length切割为两端
     *
     * @param text
     * @param length
     * @param endWith
     * @return
     */
    public static String[] splitByLength(String text, int length, String endWith) {
        int textLength = text.length();
        int byteLength = 0;
        String[] strings = new String[2];
        StringBuffer lineOne = new StringBuffer();
        StringBuffer lineEnd = new StringBuffer();
        for (int i = 0; i < textLength; i++) {
            String str_i = text.substring(i, i + 1);
            if (str_i.getBytes().length == 1) {//英文
                byteLength++;
            } else {//中文
                byteLength += 2;
            }
            if (byteLength <= length * 2)
                lineOne.append(str_i);
            else
                lineEnd.append(str_i);

        }
        try {
            if (byteLength < text.getBytes("GBK").length) {//getBytes("GBK")每个汉字长2，getBytes("UTF-8")每个汉字长度为3
                lineOne.append(endWith);
                lineEnd.append(endWith);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        strings[0] = lineOne.toString();
        strings[1] = lineEnd.toString();
        return strings;
    }

    /**
     * 获取字符串长度 两个英文字符长度为1
     *
     * @param text
     * @return
     */
    public static int getLength(String text) {

        int textLength = text.length();
        int byteLength = 0;

        for (int i = 0; i < textLength; i++) {
            String str_i = text.substring(i, i + 1);
            if (str_i.getBytes().length == 1) {//英文
                byteLength++;
            } else {//中文
                byteLength += 2;
            }

        }
        return (int)Math.ceil(byteLength/2f);
    }
}

