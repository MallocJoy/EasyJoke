package com.utouu.easyjoke;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.bgabanner.BGABanner;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.BGABanner)
    BGABanner BGABanner;
    @BindView(R.id.tv_skip)
    TextView tvSkip;
    @BindView(R.id.btn_enter)
    Button btnEnter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        //设置点击跳转的监听
        BGABanner.setEnterSkipViewIdAndDelegate(R.id.btn_enter, R.id.tv_skip, () -> {
            startActivity(new Intent(SplashActivity.this,MainActivity.class));
            finish();  });

        //设置数据
        BGABanner.setData(
                R.drawable.bg_intros1,
                R.drawable.bg_intros2,
                R.drawable.bg_intros3);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 如果引导页主题是透明的，需要在界面可见时给背景 Banner 设置一个白色背景，
        // 避免滑动过程中两个 Banner 都设置透明度后能看到 Launcher
        BGABanner.setBackgroundResource(android.R.color.white);
    }
}
