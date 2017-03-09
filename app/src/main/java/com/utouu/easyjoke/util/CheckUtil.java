package com.utouu.easyjoke.util;

import android.text.TextUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Created by 李波 on 2017/2/24.
 * Function:
 * Desc:
 */

public class CheckUtil {
    /**
     * 判断删除、取消返回值是否成功
     *
     * @param value
     * @return
     */
    public static boolean checkResponseSuccess1(String value) {
        if (TextUtils.isEmpty(value)) {
            return false;
        }
        try {
            JsonObject object = new JsonParser()
                    .parse(value).getAsJsonObject();
            int code = object.get("code").getAsInt();
            boolean success = object
                    .get("success").getAsBoolean();
            return code == 0 && success;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
