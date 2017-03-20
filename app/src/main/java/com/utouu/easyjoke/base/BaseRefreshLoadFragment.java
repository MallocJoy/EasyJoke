package com.utouu.easyjoke.base;

import android.os.Bundle;
import android.view.View;

import com.aries.ui.view.title.TitleBarView;
import com.aries.ui.widget.alert.UIAlertView;
import com.marno.easystatelibrary.EasyStatusView;
import com.marno.rapidlib.module.fragment.RapidRefreshLoadFragment;
import com.utouu.easyjoke.R;

/**
 * Created by cj on 2017/2/10.
 * Function:可以上拉加载更多下拉刷新的fragment
 * Desc:
 */

public abstract class BaseRefreshLoadFragment extends RapidRefreshLoadFragment {
    protected EasyStatusView easyStatusView;
    private View.OnClickListener mOnClickListener;
    private TitleBarView titleBar;
    private UIAlertView dialog;

    //维护页面状态
    protected void setEasyStatusView(EasyStatusView easyStatusView) {
        this.easyStatusView = easyStatusView;
    }

    @Override
    protected void initView(View view, Bundle bundle) {

        titleBar = (TitleBarView) view.findViewById(R.id.titleBar);

        if (titleBar != null) {
            initTitleBar();
            setTitleBar(titleBar);
        }
        _initView(view,bundle);

        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (easyStatusView != null) {
                    easyStatusView.loading();
                    BaseRefreshLoadFragment.this.loadData();
                }
            }
        };
        if (easyStatusView != null) {
            easyStatusView.getNoNetworkView().setOnClickListener(mOnClickListener);
            easyStatusView.getErrorView().setOnClickListener(mOnClickListener);
            easyStatusView.getEmptyView().setOnClickListener(mOnClickListener);
        }
    }

    protected abstract void _initView(View view, Bundle bundle);

    protected void setTitleBar(TitleBarView titleBar) {

    }

    private void initTitleBar() {
        titleBar.setOnLeftTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }
}
