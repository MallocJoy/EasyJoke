package com.utouu.easyjoke;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
public class ThirdPageFragment extends Fragment {

    @BindView(R.id.tv_indicate)
    TextView tvIndicate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.layout_recommend, container, false);
        ButterKnife.bind(this, rootView);
        tvIndicate.setText("这里是新鲜");
        tvIndicate.setTextColor(Color.GREEN);

        return rootView;
    }
}
