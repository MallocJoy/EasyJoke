package com.utouu.module.login.presenter;

import android.content.Context;

import com.utouu.android.commons.http.BaseCallback;
import com.utouu.android.commons.presenter.BasePresenter;
import com.utouu.android.commons.presenter.model.IRenewDevices;
import com.utouu.android.commons.presenter.model.impl.RenewDevices;
import com.utouu.module.login.presenter.view.IRenewDevicesView;

/**
 * User: Administrator
 * Date: 2016-05-26 13:52
 */
public class RenewDevicesPresenter extends BasePresenter<IRenewDevicesView> {

    private IRenewDevices renewDevices;

    public RenewDevicesPresenter(IRenewDevicesView baseView) {
        super(baseView);
        renewDevices = new RenewDevices();
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
