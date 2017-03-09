package com.utouu.easyjoke.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.google.gson.JsonSyntaxException;
import com.utouu.android.commons.constants.DataConstant;
import com.utouu.android.commons.utils.GsonUtils;
import com.utouu.android.commons.utils.PreferenceUtils;

import cn.utsoft.xunions.util.constants.XunionsPreference;


/**
 * Created by Administrator on 2016/7/14.
 */
public class XunionsUtil {


    public static String getTgt(Context context) {
        return DataConstant.getLocalTGT(context);
    }

    public static boolean isLogin(Context context) {
        return !TextUtils.isEmpty(getTgt(context));
    }

    public static void startActivityStateLogin(Context context, Class clazz) {
        if (null == context || clazz == null) {
            return;
        }

//        long userId = cn.xweed.util.PreferenceUtils.getPrefLong(context, XweedPreference.KEY_BASIC_USER_ID, -1);
//        if (-1 != userId) {
//            context.startActivity(new Intent(context, clazz));
//        } else {
//            AppUtil.startLoginActivity(context);
//            context.startActivity(new Intent(context, LoginActivity.class));
//        }
        String tgt = DataConstant.getLocalTGT(context);
        String userName = PreferenceUtils.getPrefString(context, XunionsPreference.KEY_BASIC_USER_NAME, "");
        Log.d("kecong", "tgt=" + tgt);
        if (!TextUtils.isEmpty(tgt)&&!TextUtils.isEmpty(userName)) {
            context.startActivity(new Intent(context, clazz));
        } else {
            AppUtil.startLoginActivity(context);
        }

    }


//    public static String getImgaeUrl(String url) {
//
//        if (BuildConfig.XUNIONS_DEV) {
//            url = ImagePathUtils.getUserDEVImagePath(url);
//        } else if (BuildConfig.XUNIONS_TEST) {
//            url = ImagePathUtils.getImagePath(url);
//        } else if (BuildConfig.XUNIONS_LIVE) {
//            url = ImagePathUtils.getUserImagePath(url);
//        }
//
//        return url;
//    }

    /**
     * 解析JSON
     *
     * @param context
     * @param content  JSON
     * @param classOfT 解析类
     * @param <T>
     * @return
     */
    public static <T> T fromJson(Context context, String content, Class<T> classOfT) {
        T t = null;
        try {
            t = GsonUtils.getGson().fromJson(content, classOfT);
        } catch (JsonSyntaxException e) {
            ErrorUtils.uploadException(context, content, e);
            Log.e("JsonSyntaxException-->", e.toString());
        }
        return t;
    }


    public static PopupWindow initPopupWindow(Context context, int resource, boolean outsideTouchable) {

        final View view = View.inflate(context, resource, null);

        final PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);

        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x99000000));
        popupWindow.setAnimationStyle(0);

        if (outsideTouchable) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != popupWindow && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                }
            });
        }

        return popupWindow;
    }

    public static PopupWindow initPopupWindow(Context context, int resource, final int resourceId) {

        final View view = View.inflate(context, resource, null);

        final PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);

        popupWindow.setOutsideTouchable(true);
//        popupWindow.setBackgroundDrawable(new ColorDrawable(0x99000000));
        popupWindow.setAnimationStyle(0);
//        popupWindow.setFocusable(true);
        view.setFocusableInTouchMode(true);


        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        view.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int height = view.findViewById(resourceId).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        popupWindow.dismiss();
                    }
                }
                return true;
            }
        });

        return popupWindow;
    }


//    public static LoadDataView initLoadDataView(Context context, View view, View.OnClickListener errorListner) {
//        if (null == context || null == view) {
//            return null;
//        }
//
//        if (!(view instanceof ViewGroup)) {
//            return null;
//        }
//        ViewGroup group = (ViewGroup) view;
//        ViewGroup parent = (ViewGroup) group.getParent();
//        LoadDataView loadView = null;
//
//        if (null != parent) {
//            parent.removeView(group);
//            loadView = new LoadDataView(context, group);
//            parent.addView(loadView);
//        }
//
//        if (loadView != null) {
//            loadView.setErrorListner(errorListner);
//        }
//
//        return loadView;
//
//    }
//
//
//    public static DropDownListView initDropDownListView(Context context) {
//        View view = View.inflate(context, R.layout.view_drop_down_list, null);
//        final DropDownListView dropDownListView = new DropDownListView(context, view, DrawerLayout.LayoutParams.MATCH_PARENT, DrawerLayout.LayoutParams.MATCH_PARENT, true);
//        dropDownListView.setTouchable(true);
//        dropDownListView.setFocusable(true);
//        dropDownListView.setOutsideTouchable(true);
//        dropDownListView.setAnimationStyle(0);
////            // 实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(0000);
////            // 设置SelectPicPopupWindow弹出窗体的背景
//        dropDownListView.setBackgroundDrawable(dw);
//
//        view.findViewById(R.id.fl_background).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (null != dropDownListView && dropDownListView.isShowing()) {
//                    dropDownListView.dismiss();
//                }
//            }
//        });
//
//        return dropDownListView;
//    }
//
//
//    public static boolean checkNet(Context context, LoadDataView loadDataView) {
//        if (null != context && null != loadDataView && !NetWorkUtils.isConnected(context)) {
//            loadDataView.loadNotNetwork();
//            return false;
//        }
//        return true;
//    }


    /**
     * 判断系统中是否存在某个包名的程序应用
     *
     * @param context
     * @param pkgName
     * @return
     */
    public static boolean isPkgInstalled(Context context, String pkgName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }

//    public static void startApp(final Context context, String appPackageName, final String appName, final String appURL, final int appIcon) {
//        UtouuDialog updateDialog = null;
//        try {
//            Intent intent = context.getPackageManager()
//                    .getLaunchIntentForPackage(appPackageName);
//            context.startActivity(intent);
//        } catch (Exception e) {
//            updateDialog = new UtouuDialog.Builder(context).setMessage("当前" + appName + "未安装,是否下载" + appName + "?")
//                    .setPositiveButton(android.R.string.ok,
//                            new DialogInterface.OnClickListener() {
//
//                                @Override
//                                public void onClick(DialogInterface dialog,
//                                                    int which) {
//                                    if (appURL.endsWith(".apk")) {
//                                        downApk(context, appName, appURL, appIcon);
//                                    } else {
//                                        checkUpdates(context, appName, appURL, appIcon);
//                                    }
//                                    dialog.dismiss();
//                                }
//                            })
//                    .setNegativeButton(android.R.string.cancel, null)
//                    .create();
//            if (null != updateDialog && !updateDialog.isShowing()) {
//                updateDialog.show();
//            }
//        }
//    }

//    private static void downApk(Context context, String appName, String url, int appIcon) {
//        Intent intent = new Intent(context, AppDownService.class);
//        intent.putExtra(UtouuContants.APK_DOWNLOAD_NAME, appName);
//        intent.putExtra(UtouuContants.APK_DOWNLOAD_URL, url);
//        intent.putExtra(UtouuContants.APK_DOWNLOAD_ICON, appIcon);
//        context.startService(intent);
//        ToastUtils.showLongToast(context, "开始下载, 可至通知栏查看进度...");
//    }

//    private static void checkUpdates(final Context context, final String appName, String url, final int appIcon) {
//        HashMap<String, String> params = new HashMap<String, String>();
//        params.put("version", "0");
//        UtouuHttpUtils.operation(context, url, params, new CheckLoginCallback() {
//            @Override
//            public void tgtInvalid(String message) {
//
//            }
//
//            @Override
//            public void success(String message) {
//                BaseProtocol baseProtocol = null;
//                try {
//                    baseProtocol = GsonUtils.getGson().fromJson(message, BaseProtocol.class);
//                } catch (JsonSyntaxException e) {
//                }
//
//                if (null != baseProtocol) {
//                    if (baseProtocol.success) {
//                        UpdateEntity update = null;
//                        try {
//                            update = GsonUtils.getGson().fromJson((JsonElement) baseProtocol.data, UpdateEntity.class);
//                        } catch (JsonSyntaxException e) {
//                        }
//                        if (update != null) {
//                            if (!update.upgrade) {
//                                ToastUtils.showShortToast(context, "暂不需要升级...");
//                                return;
//                            }
//                            if (TextUtils.isEmpty(update.url)) {
//                                ToastUtils.showShortToast(context, "未获取到下载地址...");
//                            } else {
//                                if (null != update.url) {
//                                    downApk(context, appName, update.url, appIcon);
//                                }
//                            }
//                        }
//                    } else {
//                        ToastUtils.showShortToast(context, baseProtocol.msg);
//                    }
//                } else {
//                    ToastUtils.showShortToast(context, "解析数据出错...");
//                }
//            }
//
//            @Override
//            public void failure(String message) {
//
//            }
//
//            @Override
//            public void failure(String statusCode, String message) {
//                failure(message);
//            }
//        });
//
//    }


}
