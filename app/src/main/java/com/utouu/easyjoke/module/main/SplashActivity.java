package com.utouu.easyjoke.module.main;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.marno.easyutilcode.IntentUtil;
import com.utouu.easyjoke.R;
import com.utouu.easyjoke.base.BaseActivity;

import butterknife.BindView;
import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Create by 黄思程 on 2017/3/27  10:48
 * Function：
 * Desc：第一次进入应用的闪屏页
 */
public class SplashActivity extends BaseActivity {
    @BindView(R.id.BGABanner)
    BGABanner BGABanner;

    @Override
    protected void _initView(Bundle bundle) {
        //设置点击跳转的监听
        SharedPreferences splash = getSharedPreferences("splash", MODE_PRIVATE);
        SharedPreferences.Editor edit = splash.edit();
        BGABanner.setEnterSkipViewIdAndDelegate(R.id.btn_enter, R.id.tv_skip, () -> {
            IntentUtil.to(this,MainActivity.class);
            edit.putBoolean("isFirst",true);
            edit.apply();
            finish();
        });

        //设置数据
        BGABanner.setData(
                R.drawable.bg_intros1,
                R.drawable.bg_intros2,
                R.drawable.bg_intros3);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 如果引导页主题是透明的，需要在界面可见时给背景 Banner 设置一个白色背景，
        // 避免滑动过程中两个 Banner 都设置透明度后能看到 Launcher
        BGABanner.setBackgroundResource(android.R.color.white);
    }
}
