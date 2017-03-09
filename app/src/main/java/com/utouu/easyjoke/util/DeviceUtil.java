package com.utouu.easyjoke.util;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * Created by cj on 2017/2/13.
 * Function:
 * Desc:设备相关的工具类
 */

public class DeviceUtil {

    /**
     * 获取手机设备的宽和高
     * @param activity
     * @return
     */
    public static int[] getDeviceWidthAndHight(Activity activity){
        int[] info = new int[2];
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        info[0] = metrics.widthPixels;
        info[1] = metrics.heightPixels;
        return info;
    }

}
