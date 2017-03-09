package com.utouu.easyjoke.base;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.aries.ui.view.title.TitleBarView;
import com.aries.ui.widget.alert.UIAlertView;
import com.marno.easystatelibrary.EasyStatusView;
import com.marno.easyutilcode.StackUtil;
import com.marno.rapidlib.basic.BasicActivity;
import com.utouu.android.commons.constants.DataConstant;

import org.simple.eventbus.EventBus;

import cn.utsoft.xunions.R;
import cn.utsoft.xunions.data.http.XunionsAsyncHttpUtils;
import cn.utsoft.xunions.util.AlertUtil;
import cn.utsoft.xunions.util.AppUtil;
import cn.utsoft.xunions.util.StatusBarUtil;


/**
 * Created by cj on 2017/2/7.
 * Function:
 * Desc:
 */

public abstract class BaseActivity extends BasicActivity {
    protected EasyStatusView easyStatusView;
    private View.OnClickListener mOnClickListener;

    TitleBarView titleBar;//标题栏
    int type = 0;
    private UIAlertView dialog;

    //维护页面状态
    protected void setEasyStatusView(EasyStatusView easyStatusView) {
        this.easyStatusView = easyStatusView;
    }


    @Override
    protected void initView(Bundle bundle) {
        //TintStatusBar.translucentStatusBar(this);
        View view = findViewById(android.R.id.content).getRootView();
        titleBar = (TitleBarView) view.findViewById(R.id.titleBar);
        if (titleBar != null) {
            if (type > 0) {
                titleBar.setImmersible(this, true);
                StatusBarUtil.StatusBarLightMode(this);
            }
            initTitleBar();
            setTitleBar(titleBar);
        }

        _initView(bundle);

        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (easyStatusView != null ) {
                    easyStatusView.loading();
                    BaseActivity.this.loadData();
                }
            }
        };

        if (easyStatusView != null) {
//            easyStatusView.setOnClickListener(mOnClickListener);
            easyStatusView.getEmptyView().setOnClickListener(mOnClickListener);
            easyStatusView.getErrorView().setOnClickListener(mOnClickListener);
            easyStatusView.getNoNetworkView().setOnClickListener(mOnClickListener);
        }
    }

    protected abstract void _initView(Bundle bundle);


    protected void setTitleBar(TitleBarView titleBar) {

    }

    /**
     * 对titlebar进行一些基本设置，如需其他设置实现setTitleBar方法即可
     */
    private void initTitleBar() {
        titleBar.setOnLeftTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //titleBar 白底黑字使用一下代码
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        type = StatusBarUtil.StatusBarDarkMode(this);
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideSoftInput();
        EventBus.getDefault().unregister(this);
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

    /**
     * 请求后令牌失效时调用的方法
     *
     * @param message
     */
    public void tgtInvalid(String message) {

        dialog = AlertUtil.show(mContext, message, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                XunionsAsyncHttpUtils.clearST();
                DataConstant.saveLocalTGT(mContext, "");
                StackUtil.getIns().popAll();
                AppUtil.startLoginActivity(mContext);
            }
        });
    }
}
