package com.utouu.module.login.presenter.view;

import com.utouu.android.commons.presenter.view.IBaseView;

/**
 * 绑定平台账号
 * User: yang
 * Date: 2016-03-03 20:16
 */
public interface IBindAccountView extends IBaseView {

    /**
     * 成功
     *
     * @param content
     */
    void success(String content);

    /**
     * 失败
     *
     * @param message
     */
    void failure(String message);

    /**
     * 失败
     *
     * @param errorCode
     * @param message
     */
    void failure(String errorCode, String message);
}
