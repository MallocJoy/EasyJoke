package com.utouu.easyjoke.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.aries.ui.view.title.TitleBarView;
import com.marno.easystatelibrary.EasyStatusView;
import com.marno.rapidlib.basic.BasicFragment;
import com.marno.rapidlib.enums.RxLifeEvent;
import com.utouu.easyjoke.R;


import rx.Observable;

/**
 * Create by 黄思程 on 2017/3/20  9:41
 * Function：
 * Desc：基类Activity
 */
public abstract class BaseFragment extends BasicFragment {
    protected EasyStatusView easyStatusView;
    private View.OnClickListener mOnClickListener;
    private TitleBarView titleBar;

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

        Observable.Transformer<Object, Object>
                objectObjectTransformer = bindUntilEvent(RxLifeEvent.DESTROY);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (easyStatusView != null) {
                    easyStatusView.loading();
                    BaseFragment.this.loadData();
                }
            }
        };

        _initView(view, bundle);

        if (easyStatusView != null) {
//            easyStatusView.setOnClickListener(mOnClickListener);
            easyStatusView.getEmptyView().setOnClickListener(mOnClickListener);
            easyStatusView.getErrorView().setOnClickListener(mOnClickListener);
            easyStatusView.getNoNetworkView().setOnClickListener(mOnClickListener);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    protected void showKeyboard(Activity activity, boolean isShow) {
        activity = getActivity();
        if (activity == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        if (isShow) {
            if (activity.getCurrentFocus() == null) {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            } else {
                imm.showSoftInput(activity.getCurrentFocus(), 0);
            }
        } else {
            if (getActivity().getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

}
