package com.utouu.easyjoke;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by 黄思程 on 2016/12/16   10:36
 * Function：
 * Desc：
 */
public class SecondPageFragment extends Fragment {

    @BindView(R.id.tv_indicate)
    TextView tvIndicate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.layout_recommend, container, false);
        ButterKnife.bind(this, rootView);

        SpannableString str = new SpannableString("这里是发现页面");
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#009ad6"));
        str.setSpan(colorSpan,0,2, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        tvIndicate.setText(str);

        return rootView;
    }

}
