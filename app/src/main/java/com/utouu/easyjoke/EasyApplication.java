package com.utouu.easyjoke;

import android.app.Application;
import android.content.Context;

import me.drakeet.library.CrashWoodpecker;
import me.drakeet.library.PatchMode;

/**
 * Create by 黄思程 on 2016/12/15   16:20
 * Function：
 * Desc：
 */
public class EasyApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getAppContext();

        //内存泄漏检查初始化
        CrashWoodpecker.instance()
                .withKeys("widget", "me.drakeet")
                .setPatchMode(PatchMode.SHOW_LOG_PAGE)
                .setPatchDialogUrlToOpen("https://drakeet.me")
                .setPassToOriginalDefaultHandler(true)
                .flyTo(this);
    }

    //全局获取ApplicationContext
    public static Context getAppContext() {
        return mContext;
    }

}
