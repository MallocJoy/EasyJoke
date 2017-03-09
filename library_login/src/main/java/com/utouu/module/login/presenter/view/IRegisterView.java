package com.utouu.module.login.presenter.view;

import android.graphics.Bitmap;

/**
 * User: yang
 * Date: 2016-03-11 03:41
 */
public interface IRegisterView extends IVerifyView {

    /**
     * 注册成功
     *
     * @param message
     */
    void registerSuccess(String message);

    /**
     * 注册失败
     *
     * @param message
     */
    void registerFailure(String message);

    /**
     * 注册失败
     *
     * @param errorCode
     * @param message
     */
    void registerFailure(String errorCode, String message);

    /**
     * 获取验证码成功
     *
     * @param bitmap
     */
    void smsImageVifySuccess(Bitmap bitmap);

    /**
     * 获取验证码失败
     *
     * @param message
     */
    void smsImageVifyFailure(String message);

    /**
     * 短信发送成功
     */
    void sendSMSSuccess(String message);

    /**
     * 短信发送失败
     */
    void sendSMSFailure(String message);
}
