package com.utouu.module.login.presenter.view;

import com.utouu.android.commons.presenter.view.IBaseView;

/**
 * User: Administrator
 * Date: 2016-05-26 13:32
 */
public interface IRenewDevicesView extends IBaseView {

    /**
     * 更换设备获取短信成功
     *
     * @param message
     */
    void sendSMSSuccess(String message);

    /**
     * 更换设备获取短信失败
     *
     * @param message
     */
    void sendSMSFailure(String message);
}

