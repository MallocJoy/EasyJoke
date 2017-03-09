package com.utouu.module.login.presenter;

import android.content.Context;

import com.utouu.android.commons.entity.LoginParams;
import com.utouu.android.commons.http.BaseCallback;
import com.utouu.android.commons.presenter.BasePresenter;
import com.utouu.android.commons.presenter.model.ILoginAccount;
import com.utouu.android.commons.presenter.model.IRenewDevices;
import com.utouu.android.commons.presenter.model.impl.RenewDevices;
import com.utouu.module.login.presenter.view.ILoginView;
import com.utouu.module.login.ui.fragment.LoginAccountNew;

/**
 * User: yang
 * Date: 2016-03-09 18:43
 */
public class LoginPresenter extends BasePresenter<ILoginView> {

    private ILoginAccount loginAccount;
    private IRenewDevices renewDevices;

    public LoginPresenter(ILoginView baseView) {
        super(baseView);
        loginAccount = new LoginAccountNew();
        renewDevices = new RenewDevices();
    }

    /**
     * 获取TGT
     */
    public void getTGT(final Context context, LoginParams loginParams) {
        if (baseView != null && context != null) {
            loginAccount.getTGT(context, loginParams, new ILoginAccount.Callback() {
                @Override
                public void success(String account, String password, String message) {
                    if (baseView != null) {
                        baseView.tgtSuccess(account, password, message);
                    }
                }

                @Override
                public void failure(String message) {
                    if (baseView != null) {
                        baseView.tgtFailure(message);
                    }
                }

                @Override
                public void failure(String code, String message) {
                    if (baseView != null) {
                        baseView.tgtFailure(code, message);
                    }
                }
            });
        }
    }

    public void sendSMS(Context context, String mobile) {
        if (baseView != null && context != null) {
            renewDevices.sendSMS(context, mobile, new BaseCallback() {
                @Override
                public void success(String message) {
                    if (baseView != null) {
                        baseView.sendSMSSuccess(message);
                    }
                }

                @Override
                public void failure(String message) {
                    if (baseView != null) {
                        baseView.sendSMSFailure(message);
                    }
                }

                @Override
                public void failure(String statusCode, String message) {
                    failure(message);
                }
            });
        }
    }
}
