package com.utouu.module.login.presenter.view;


/**
 * Created by yang on 15/12/19.
 */
public interface ILoginView extends IRenewDevicesView {

    /**
     * 登录成功
     *
     * @param account
     * @param password
     * @param message
     */
    void tgtSuccess(String account, String password, String message);

    /**
     * 登录失败
     *
     * @param message
     */
    void tgtFailure(String message);

    /**
     * 登录失败
     *
     * @param errorCode <p>-10   正在进行更换设备操作</p>
     *                  <p>-11   未绑定第三方登录账号</p>
     *                  <p>-522  需要重新认证协议</p>
     * @param message
     */
    void tgtFailure(String errorCode, String message);
}
