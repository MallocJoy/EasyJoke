package com.utouu.easyjoke.util;

import java.security.MessageDigest;

/**
 * Created by chenxin on 2017/2/7.
 * Fuction:
 * Desc:
 */

public class MD5Util {
    public MD5Util() {
    }

    public static final String md5x2(String s) {
        return md5(md5(s));
    }

    public static final String md5(String s, int b) {
        try {
            MessageDigest e = MessageDigest.getInstance("MD5");
            e.update(s.getBytes());
            switch(b) {
                case 16:
                    return getEncode16(e);
                case 32:
                default:
                    return md5(s);
                case 64:
                    return null;
            }
        } catch (Exception var3) {
            return null;
        }
    }

    public static final String md5(String s) {
        char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        try {
            byte[] e = s.getBytes("UTF-8");
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(e);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;

            for(int i = 0; i < j; ++i) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 15];
                str[k++] = hexDigits[byte0 & 15];
            }

            return new String(str);
        } catch (Exception var10) {
            return null;
        }
    }

    private static String getEncode16(MessageDigest digest) {
        StringBuilder builder = new StringBuilder();
        byte[] var2 = digest.digest();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            byte b = var2[var4];
            builder.append(Integer.toHexString(b >> 4 & 15));
            builder.append(Integer.toHexString(b & 15));
        }

        return builder.substring(8, 24).toString();
    }
}
