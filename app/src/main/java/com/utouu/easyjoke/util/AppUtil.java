package com.utouu.easyjoke.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import com.utouu.easyjoke.BuildConfig;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class AppUtil {

    /**
     * 调整当前页面亮度
     *
     * @param activity
     * @param brightness
     */
    public static void changeAppBrightness(Activity activity, int brightness) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        if (brightness == -1) {
            lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
        } else {
            lp.screenBrightness = (brightness <= 0 ? 1 : brightness) / 255f;
        }
        window.setAttributes(lp);
    }

    /**
     * 调整音量
     *
     * @param context
     * @param streamType
     * @param aduioVolume
     */
    public static void changeAppAudioVolume(Context context, int streamType, int aduioVolume) {
        AudioManager adAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        adAudioManager.setStreamVolume(streamType, aduioVolume, 0);

    }

    /**
     * 获取最大音量
     *
     * @param context
     * @param streamType
     * @return
     */
    public static int getMaxAudioVolume(Context context, int streamType) {
        AudioManager adAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return adAudioManager.getStreamMaxVolume(streamType);
    }

    /**
     * 获取当前音量
     *
     * @param context
     * @param streamType
     * @return
     */
    public static int getCurrentAudioVolume(Context context, int streamType) {
        AudioManager adAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return adAudioManager.getStreamVolume(streamType);
    }

    /**
     * 清除应用缓存
     *
     * @param context
     */
    public static void clearCache(Context context) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            deleteFilesByDirectory(context.getExternalCacheDir());
        }
    }

    private static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }


    /**
     * 获取设备IMEI号
     *
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {

        String imei = "";
        try {
            // 获取设备信息
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (null != tm) {
                imei = tm.getDeviceId();
            }
            if (null == imei) {
                imei = "";
            }

            // getLine1Number 手机号
            // getSimSerialNumber SIM卡序号
            // getSubscriberId IMSI

        } catch (SecurityException e) {
            ErrorUtils.uploadException(context, "getIMEI->获取设备信息出错，缺少权限.", e);
        } catch (Exception e) {
            ErrorUtils.uploadException(context, "获取设备信息出错.", e);
        }
        return imei;
    }


    /**
     * 校验设备
     *
     * @return
     */
    public static boolean validateDevices(Context context) {
        /*String imei = DeviceUtils.getIMEI(context);
        if (StringUtils.isBlank(imei)) {
            ToastUtils.showShortToast(context, "无法识别手机身份, 请不要禁用读取IMEI码权限！");
            return false;
        }
        if (DeviceUtils.checkIMEISame(imei) || DeviceUtils.checkVirtualDevice()) {
            ToastUtils.showShortToast(context, "请使用真实设备！");
            return false;
        }*/
        return true;
    }


    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            if (null != packageManager) {
                PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
                if (null != packageInfo) {
                    int labelRes = packageInfo.applicationInfo.labelRes;
                    return context.getResources().getString(labelRes);
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            ErrorUtils.uploadException(context, "AppUtils.getAppName", e);
        }
        return "XWEED";
    }


    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            if (null != packageManager) {
                PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
                if (null != packageInfo) {
                    return packageInfo.versionName;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            ErrorUtils.uploadException(context, "AppUtils.getVersionName", e);
        }
        return BuildConfig.VERSION_NAME;
    }


    /**
     * 获取当前网络类型
     * @return 0：没有网络   1：WIFI网络   2：WAP网络    3：NET网络
     */

    public static final int NETTYPE_WIFI = 0x01;
    public static final int NETTYPE_CMWAP = 0x02;
    public static final int NETTYPE_CMNET = 0x03;
    public int getNetworkType(Context context) {
        int netType = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if(!TextUtils.isEmpty(extraInfo)){
                if (extraInfo.toLowerCase().equals("cmnet")) {
                    netType = NETTYPE_CMNET;
                } else {
                    netType = NETTYPE_CMWAP;
                }
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = NETTYPE_WIFI;
        }
        return netType;
    }

    /**
     * 模拟器判断
     *
     * @param context
     * @return <br>
     * 创建时间：2015-6-19 下午3:23:34
     */
    public static void isEmulator(Context context) {
        String result = "";
        try {
            String[] args = {"/system/bin/cat", "/proc/cpuinfo"};
            ProcessBuilder cmd = new ProcessBuilder(args);

            Process process = cmd.start();
            StringBuffer sb = new StringBuffer();
            String readLine = "";
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "utf-8"));
            while ((readLine = responseReader.readLine()) != null) {
                sb.append(readLine);
            }
            responseReader.close();
            result = sb.toString().toLowerCase();
        } catch (IOException ex) {
        }

        boolean flag = (!result.contains("arm")) || (result.contains("intel")) || (result.contains("amd"));
        ErrorUtils.uploadException(context, "devices", new Exception("FLAG ->" + flag + ", result ->" + result));
//		return flag;
    }


    /*public static void startLoginActivity(Context context) {
        BaseLoginActivity.start(context,LoginActivity.class, BuildConfig.XUNIONS_TYPE,"DEFAULT_UDID","DEFAULT_TOKEN","XUNIONS", BuildConfig.VERSION_NAME,true,2);
//        BaseLoginActivity.start(context,LoginActivity.class,BuildConfig.XUNIONS_TYPE,"DEFAULT_UDID","DEFAULT_TOKEN","XUNIONS", BuildConfig.VERSION_NAME,false);
    }*/

    public static String getWindowWidth(Activity activity) {

        DisplayMetrics dm = new DisplayMetrics();
        //获取屏幕信息
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

        int screenWidth = dm.widthPixels;

        return String.valueOf(screenWidth);
    }

    public static String getWindowHeight(Activity activity) {

        DisplayMetrics dm = new DisplayMetrics();
        //获取屏幕信息
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

        int screenHeigh = dm.heightPixels;

        return String.valueOf(screenHeigh);
    }


    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getWidth();
    }

    /**
     * 获取屏幕的高度
     */

    public static int getScreenHeight(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getHeight();
    }


    public static void dialPhoneNumber(Context context,String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }


    /**
     * 清空SD卡缓存图片
     * <p/>
     * <br>
     * 创建时间：2015-7-23 下午3:51:31
     */
//    public static void clearSDCache() {
//        ImageLoader.getInstance().clearDiskCache();
//    }

    /**
     * 清空SD卡中当前应用建立的文件夹 <br>
     * 创建时间：2015-7-23 下午3:56:06
     */
    public static void clearSDFile() {
        // 删除数据
        delAllFile(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "xunions");

    }


    /**
     * 清除sp
     *
     * @param context
     */
    public static void clearSharedPreferences(Context context) {
        /*PreferenceUtils.clear(context);*/
    }

    /**
     * 删除指定文件夹下所有文件
     *
     * @param path 文件夹完整绝对路径
     * @return <br>
     * 创建时间：2015-7-23 下午3:53:06
     */
    public static boolean delAllFile(String path) {

        File file = new File(path);
        if (!file.exists()) {
            return false;
        }
        if (!file.isDirectory()) {
            return false;
        }

        boolean flag = false;
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + File.separator + tempList[i]);// 先删除文件夹里面的文件
                delFolder(path + File.separator + tempList[i]);// 再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 删除文件夹
     *
     * @param folderPath 文件夹完整绝对路径 <br>
     *                   创建时间：2015-7-23 下午3:52:48
     */
    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); // 删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            myFilePath.delete(); // 删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @param string 例："0.00"保留两位小数
     * @param vaule 值
     * @return
     */
    public static String decimals(String string,Double vaule) {
        DecimalFormat df = new DecimalFormat(string);
        String vaule_ = df.format(vaule);
        return vaule_;
    }

    /**
     * @param string 例："2"保留两位小数 四舍五入
     * @param vaule 值
     * @return
     */
    public static String decimals(int string,Double vaule) {

        DecimalFormat formater = new DecimalFormat();
        //保留几位小数
        formater.setMaximumFractionDigits(string);
        //模式  四舍五入
        formater.setRoundingMode(RoundingMode.UP);
        String vaule_ =  formater.format(vaule);

        return vaule_;
    }


//    /**
//     * 分享
//     *
//     * @param shareUrl    分享的地址
//     * @param missionName 分享的名称 //R.mipmap.logo
//     */
//    public static void shareClick(final Activity activity, String shareUrl, String missionName, String content,int imageId) {
//
////        String userVisitorCode = PreferenceUtils.getPrefString(this, UtouuPreference.KEY_BASIC_USER_VISITOR_CODE, "-1");
////
////        String url = BaseHttpURL.MOBILE_LOAD_URL + "/share/" + missionId + File.separator + userVisitorCode;
//
//        String shareTitle = missionName;
//
////        String content = "#UTOUU# 分享网络 分享未来";
//
//
//        ShareHelper.defaultShare(activity, shareTitle, content, shareUrl, new UMImage(activity, imageId), new UMShareListener() {
//            @Override
//            public void onResult(SHARE_MEDIA platform) {
//                ToastUtils.showLongToast(activity, platform + " 分享成功.");
//            }
//
//            @Override
//            public void onError(SHARE_MEDIA platform, Throwable t) {
//                ToastUtils.showLongToast(activity, platform + " 分享失败.");
//            }
//
//            @Override
//            public void onCancel(SHARE_MEDIA platform) {
//                ToastUtils.showLongToast(activity, platform + " 分享取消.");
//            }
//        });
//    }

    /**
     * Android6.0 获取权限读取外部存储的问题
     * @param activity
     * @return
     */
    public static boolean isGrantExternalRW(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            activity.requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);

            return false;
        }

        return true;
    }


}
