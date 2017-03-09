package com.utouu.easyjoke.base;

/**
 * Created by cj on 2017/2/8.
 * Function:
 * Desc:每个页面的
 */

public interface IBaseStatusView extends IBaseListener {
    void noNet();//没有网络的回调

    void empty();//无数据的回调

    void loading();//正在加载的回调

    void error(String msg);//加载数据错误的回调

    void content();//显示数据的回调
}
