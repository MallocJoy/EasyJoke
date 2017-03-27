package com.utouu.easyjoke.module.discover;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.utouu.easyjoke.R;
import com.utouu.easyjoke.base.BaseFragment;

import butterknife.BindView;

/**
 * Create by 黄思程 on 2016/12/16   10:36
 * Function：
 * Desc：
 */
public class DiscoverFragment extends BaseFragment {

    @BindView(R.id.tv_indicate)
    TextView tvIndicate;

    @Override
    protected int getLayout() {
        return R.layout.layout_recommend;
    }

    @Override
    protected void _initView(View view, Bundle bundle) {
        SpannableString str = new SpannableString("这里是发现页面");
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#009ad6"));
        str.setSpan(colorSpan,0,2, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        tvIndicate.setText(str);
    }
}
