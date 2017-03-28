package com.utouu.easyjoke.module.main;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.marno.easyutilcode.IntentUtil;
import com.orhanobut.logger.Logger;
import com.utouu.easyjoke.R;
import com.utouu.easyjoke.base.BaseActivity;
/**
 * Create by 黄思程 on 2017/3/27  10:47
 * Function：
 * Desc：欢迎页
 */
public class WelcomeActivity extends BaseActivity {

    @Override
    protected void _initView(Bundle bundle) {
        SharedPreferences splash = getSharedPreferences("splash", MODE_PRIVATE);
        boolean isFirst = splash.getBoolean("isFirst", false);
        Logger.d("启动欢迎页");

        if (!isFirst){
            IntentUtil.to(this,SplashActivity.class);
            finish();
        }else {
            new CountDownTimer(2000,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    Logger.e("剩余"+millisUntilFinished/1000+"s跳转");
                }

                @Override
                public void onFinish() {
                    IntentUtil.to(WelcomeActivity.this,MainActivity.class);
                    finish();
                }
            }.start();
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_welcome;
    }
}
