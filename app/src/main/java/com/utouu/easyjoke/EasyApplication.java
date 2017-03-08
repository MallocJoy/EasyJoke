package com.utouu.easyjoke;

import android.app.Application;

import me.drakeet.library.CrashWoodpecker;
import me.drakeet.library.PatchMode;

/**
 * Create by 黄思程 on 2016/12/15   16:20
 * Function：
 * Desc：
 */
public class EasyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //内存泄漏检查初始化
        CrashWoodpecker.instance()
                .withKeys("widget", "me.drakeet")
                .setPatchMode(PatchMode.SHOW_LOG_PAGE)
                .setPatchDialogUrlToOpen("https://drakeet.me")
                .setPassToOriginalDefaultHandler(true)
                .flyTo(this);


    }
}
