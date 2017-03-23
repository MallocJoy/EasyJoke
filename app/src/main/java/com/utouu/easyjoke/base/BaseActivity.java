package com.utouu.easyjoke.base;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import com.allen.library.SuperTextView;
import com.flyco.tablayout.SegmentTabLayout;
import com.marno.easystatelibrary.EasyStatusView;
import com.marno.rapidlib.basic.BasicActivity;
import com.utouu.easyjoke.R;
import com.utouu.easyjoke.util.StatusBarUtil;

/**
 * Create by 黄思程 on 2017/3/20  9:37
 * Function：
 * Desc：基类Activity
 */
public abstract class BaseActivity extends BasicActivity {
    protected EasyStatusView easyStatusView;
    private View.OnClickListener mOnClickListener;
    protected SuperTextView titleText;
    protected SegmentTabLayout titleSegment;

    int type = 0;

    //维护页面状态
    protected void setEasyStatusView(EasyStatusView easyStatusView) {
        this.easyStatusView = easyStatusView;
    }


    @Override
    protected void initView(Bundle bundle) {
        //TintStatusBar.translucentStatusBar(this);
        View view = findViewById(android.R.id.content).getRootView();
        RelativeLayout titleBar = (RelativeLayout) view.findViewById(R.id.titleBar);
        titleText = (SuperTextView) view.findViewById(R.id.titleText);
        titleSegment = (SegmentTabLayout) view.findViewById(R.id.titleSegment);
        if (titleBar != null) {
            initTitleBar();
            setTitleBar(titleText,titleSegment);
        }

        _initView(bundle);

        mOnClickListener = v -> {
            if (easyStatusView != null) {
                easyStatusView.loading();
                BaseActivity.this.loadData();
            }
        };

        if (easyStatusView != null) {
            easyStatusView.getEmptyView().setOnClickListener(mOnClickListener);
            easyStatusView.getErrorView().setOnClickListener(mOnClickListener);
            easyStatusView.getNoNetworkView().setOnClickListener(mOnClickListener);
        }
    }

    protected abstract void _initView(Bundle bundle);


    protected void setTitleBar(SuperTextView titleText,SegmentTabLayout titleSegment) {

    }

    /**
     * 对titlebar进行一些基本设置，如需其他设置实现setTitleBar方法即可
     */
    protected void initTitleBar() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        type = StatusBarUtil.StatusBarDarkMode(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideSoftInput();
    }

    /**
     * 界面关闭的时候隐藏软键盘
     */
    public void hideSoftInput() {
        View view = getWindow().peekDecorView();
        InputMethodManager inputManger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (view != null) {
            view.requestFocus();
            inputManger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
