package com.utouu.easyjoke.util;

import android.app.Activity;
import android.util.DisplayMetrics;

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
