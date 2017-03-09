package com.utouu.easyjoke.base;

import android.os.Bundle;
import android.view.View;

import com.marno.easyutilcode.ToastUtil;

/**
 * Created by llh on 2017/2/15 15:22.
 * Function:
 * Desc:
 */

public abstract class BaseMVPRefreshLoadFragment<V, T extends BasePresenter<V>> extends BaseRefreshLoadFragment implements IBaseStatusView {

    protected T mPresenter;

    @Override
    protected void _initView(View view, Bundle bundle) {
        //创建代理
        mPresenter = createPresenter();
        //创建关联
        mPresenter.attachView((V) this);
    }

    protected abstract T createPresenter();

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detacheView();
        }
    }

    @Override
    public void noNet() {
        if (easyStatusView != null) {
            easyStatusView.noNet();
        }
        ToastUtil.show("请检查当前网络状态");
    }

    @Override
    public void empty() {
        if (easyStatusView != null) {
            easyStatusView.empty();
        }
    }

    @Override
    public void loading() {
        if (easyStatusView != null) {
            easyStatusView.loading();
        }
    }

    @Override
    public void error(String msg) {
        if (easyStatusView != null) {
            easyStatusView.error();
        }
        ToastUtil.show(msg);
    }

    @Override
    public void content() {
        if (easyStatusView != null) {
            easyStatusView.content();
        }
    }
}
