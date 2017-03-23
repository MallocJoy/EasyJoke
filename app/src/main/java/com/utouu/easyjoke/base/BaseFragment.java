package com.utouu.easyjoke.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import com.allen.library.SuperTextView;
import com.flyco.tablayout.SegmentTabLayout;
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

    protected SuperTextView titleText;
    protected SegmentTabLayout titleSegment;

    //维护页面状态
    protected void setEasyStatusView(EasyStatusView easyStatusView) {
        this.easyStatusView = easyStatusView;
    }

    @Override
    protected void initView(View view, Bundle bundle) {
        RelativeLayout titleBar = (RelativeLayout) view.findViewById(R.id.titleBar);
        titleText = (SuperTextView) view.findViewById(R.id.titleText);
        titleSegment = (SegmentTabLayout) view.findViewById(R.id.titleSegment);
        if (titleBar != null) {
            initTitleBar();
            setTitleBar(titleText,titleSegment);
        }

        Observable.Transformer<Object, Object>
                objectObjectTransformer = bindUntilEvent(RxLifeEvent.DESTROY);
        mOnClickListener = v -> {
            if (easyStatusView != null) {
                easyStatusView.loading();
                BaseFragment.this.loadData();
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

    protected void setTitleBar(SuperTextView titleText,SegmentTabLayout titleSegment) {

    }

    private void initTitleBar() {
        //可重写此方法来对TitleBar做初始化处理
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
