package com.utouu.module.login.presenter;

import android.content.Context;
import android.graphics.Bitmap;

import com.utouu.android.commons.presenter.BasePresenter;
import com.utouu.android.commons.presenter.model.IVerificationCode;
import com.utouu.android.commons.presenter.model.impl.VerificationCode;
import com.utouu.module.login.presenter.view.IVerifyView;

/**
 * User: yang
 * Date: 2016-03-11 03:40
 */
public class VerifyPresenter extends BasePresenter<IVerifyView> {

    private IVerificationCode verificationCode;

    public VerifyPresenter(IVerifyView baseView) {
        super(baseView);
        this.verificationCode = new VerificationCode();
    }

    public void getImageVify(final Context context, String source, String verifyKey) {
        if (baseView != null && context != null) {
            verificationCode.getImageVify(context, verifyKey, source, new IVerificationCode.Callback() {
                @Override
                public void failure(String message) {
                    if (baseView != null) {
                        baseView.verifyFailure(message);
                    }
                }

                @Override
                public void success(Bitmap bitmap) {
                    if (baseView != null) {
                        baseView.verifySuccess(bitmap);
                    }
                }
            });
        }
    }
}
