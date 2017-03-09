package com.utouu.easyjoke.util;


import com.orhanobut.logger.Logger;

import cn.utsoft.xunions.BuildConfig;

/**
 * Created by cj on 2017/2/16.
 * Function:
 * Desc:
 */

public class LoggerUtil {

    public static void i(String msg) {
        if (BuildConfig.environment != 2)
        Logger.i(msg);
    }

    public static void d(String msg) {
        if (BuildConfig.environment != 2)
        Logger.d(msg);
    }

    public static void e(String msg) {
        if (BuildConfig.environment != 2)
        Logger.e(msg);
    }

    public static void json(String jsonStr) {
        if (BuildConfig.environment != 2)
        Logger.json(jsonStr);
    }

}
