package com.utouu.module.login.ui;

/**
 * Created by chenxin on 2017/3/2.
 * Function:成功与失败的监听
 * Desc:
 */

public interface PaymentListener {
    void onSuccess();

    void onFailure(String var1);

    void onCancel();
}
