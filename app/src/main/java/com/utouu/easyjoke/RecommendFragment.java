package com.utouu.easyjoke;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Create by 黄思程 on 2016/12/16   10:36
 * Function：
 * Desc：
 */
public class RecommendFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.layout_recommend, container, false);

        return rootView;
    }

}
