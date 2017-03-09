package com.utouu.easyjoke.base;

import com.marno.easystatelibrary.EasyStatusView;

import java.util.List;

/**
 * Created by 李波 on 2017/2/21.
 * Function:
 * Desc:
 */
public interface IBasePageView<T> {

    /**
     * 返回成功后的数据
     *
     * @param data
     */
    void onDataBack(boolean loadMore, List<T> data);

    /**
     * 当请求结束的时候调用,
     * 不论成功或失败
     * 如果成功会在返回成功数据后回调
     */
    void onLoadFinish(boolean success);

    /**
     * 当TGT失效时调用
     *
     * @param message
     */
    void tgtInvalid(String message);

    /**
     * 判断当前界面是否已有加载数据
     * false将显示加载界面
     * true 将不会显示加载界面
     *
     * @return
     */
    boolean hasDate();

    /**
     * 如果传入状态控件, 将由BasePagePresenter控制界面展示
     *
     * @return
     */
    EasyStatusView getStatusView();
}
