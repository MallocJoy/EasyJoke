package com.utouu.module.login.presenter.view;

import android.graphics.Bitmap;

import com.utouu.android.commons.presenter.view.IBaseView;

/**
 * User: yang
 * Date: 2016-03-11 04:29
 */
public interface IVerifyView extends IBaseView {

    /**
     * 获取验证码成功
     *
     * @param bitmap
     */
    void verifySuccess(Bitmap bitmap);

    /**
     * 获取验证码失败
     *
     * @param message
     */
    void verifyFailure(String message);
}
