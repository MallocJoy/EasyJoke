package com.utouu.easyjoke.base;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import com.allen.library.SuperTextView;
import com.flyco.tablayout.SegmentTabLayout;
import com.marno.easystatelibrary.EasyStatusView;
import com.marno.rapidlib.module.activity.RapidRefreshLoadActivity;
import com.utouu.easyjoke.R;
import com.utouu.easyjoke.util.StatusBarUtil;

import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrUIHandler;

/**
 * Created by llh on 2016/11/3 10:54.
 * Function: 可下拉刷新和加载更多的activity
 * Desc:
 */
public abstract class BaseRefreshAndLoadActivity extends RapidRefreshLoadActivity {
    protected EasyStatusView easyStatusView;
    private View.OnClickListener mOnClickListener;
    SuperTextView titleText;
    SegmentTabLayout titleSegment;
    int type = 0;

    //维护页面状态
    protected void setEasyStatusView(EasyStatusView easyStatusView) {
        this.easyStatusView = easyStatusView;
    }

    /**
     * 获取下拉刷新的头部View
     *
     * @return
     */
    @Override
    public PtrUIHandler getRefreshHeader() {
        return new PtrClassicDefaultHeader(mContext);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        type = StatusBarUtil.StatusBarDarkMode(this);
        super.onCreate(savedInstanceState);
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
                BaseRefreshAndLoadActivity.this.loadData();
            }
        };
        if (easyStatusView != null) {
            easyStatusView.setOnClickListener(mOnClickListener);
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
    protected void onDestroy() {
        super.onDestroy();
        hideSoftInput();
    }

    /**
     * 界面关闭的时候隐藏软键盘
     */
    public void hideSoftInput() {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (!manager.isActive()) {
            manager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
