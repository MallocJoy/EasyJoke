package com.utouu.module.login.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.utouu.android.commons.constants.HttpURLConstant;
import com.utouu.android.commons.http.BaseCallback;
import com.utouu.android.commons.http.UtouuHttpUtils;
import com.utouu.android.commons.presenter.BasePresenter;
import com.utouu.android.commons.presenter.model.IVerificationCode;
import com.utouu.android.commons.presenter.model.impl.RegisterAccount;
import com.utouu.android.commons.presenter.model.impl.SendSMS;
import com.utouu.android.commons.presenter.model.impl.VerificationCode;
import com.utouu.android.commons.utils.GenerateSignDemo;
import com.utouu.android.commons.utils.NetWorkUtils;
import com.utouu.module.login.presenter.view.IRegisterView;

import java.util.HashMap;

/**
 * User: yang
 * Date: 2016-03-11 03:40
 */
public class RegisterPresenter extends BasePresenter<IRegisterView> {

    private IVerificationCode verificationCode;
    private RegisterAccount registerAccount;
    private SendSMS sendSMS;

    public RegisterPresenter(IRegisterView baseView) {
        super(baseView);
        this.verificationCode = new VerificationCode();
        this.registerAccount = new RegisterAccount();
    }

    /**
     * 注册账户
     *
     * @param context
     * @param account    用户名
     * @param password   密码
     * @param source
     * @param cipher     暗号
     * @param imgCodeKey 验证码key
     * @param imgCode    短信验证码
     */
    public void register(final Context context, String account, String password, String source, String cipher, String smsCode,
                         String imgCodeKey, String imgCode) {
        if (baseView != null && context != null) {

            registerAccount
                    .register(context, account, password, source, "", cipher, smsCode, imgCode, imgCodeKey, new BaseCallback() {
                        @Override
                        public void success(String message) {
                            if (null != baseView) {
                                baseView.registerSuccess(message);
                            }
                        }

                        @Override
                        public void failure(String message) {
                            if (null != baseView) {
                                baseView.registerFailure(message);
                            }
                        }

                        @Override
                        public void failure(String statusCode, String message) {
                            if (baseView != null) {
                                baseView.registerFailure(statusCode, message);
                            }
                        }
                    });
        }
    }


    /**
     * 注册账户
     * @param context
     * @param account 账号
     * @param password 密码
     * @param source 注册来源
     * @param cipher 中文暗号
     * @param smsCode 短信验证码
     */
    public void register(final Context context, String account, String password, String source, String cipher, String smsCode) {

        if (baseView != null && context != null) {

            registerAccount.register(context, account, password, source, "", cipher, smsCode, new BaseCallback() {

                @Override
                public void success(String message) {
                    if (null != baseView) {
                        baseView.registerSuccess(message);
                    }
                }

                @Override
                public void failure(String message) {
                    if (null != baseView) {
                        baseView.registerFailure(message);
                    }
                }

                @Override
                public void failure(String statusCode, String message) {
                    if (baseView != null) {
                        baseView.registerFailure(statusCode, message);
                    }
                }
            });
        }
    }

    public void getImageVify(final Context context, String source, String verifyKey) {
        if (baseView != null && context != null) {

            if (!NetWorkUtils.isConnected(context)) {
                baseView.verifyFailure(UtouuHttpUtils.NETWORK_FAILURE);
                return;
            }

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

    public void getSmsImageVify(Context context, String source, String key) {
        verificationCode.getImageVify(context, key, source, new IVerificationCode.Callback() {

            @Override
            public void success(Bitmap bitmap) {
                if (baseView != null) {
                    baseView.smsImageVifySuccess(bitmap);
                }
            }

            @Override
            public void failure(String message) {
                if (baseView != null) {
                    baseView.smsImageVifyFailure(message);
                }
            }
        });
    }

    /**
     * 发送短信(注册帐号)
     *
     * @param context
     * @param mobile      帐号
     * @param deviceType  2:iphone,3:android,6:微信
     * @param imgVCodeKey 图形验证码key
     * @param imgVCode    图形验证码
     */
    public void sendSMS(final Context context, String mobile, int deviceType, String imgVCodeKey, String imgVCode, String imei) {
        if (baseView != null) {

            HashMap<String, String> params = new HashMap<>();
            params.put("mobile", mobile);
            params.put("device", String.valueOf(deviceType));
            params.put("imgVCodeKey", imgVCodeKey);
            params.put("imgVCode", imgVCode);
            params.put("imei", imei);

            long time = System.currentTimeMillis();
            params.put("sign", GenerateSignDemo.generateCommonSign(time, params));
            params.put("time", Long.toString(time));

            UtouuHttpUtils.operation(context, HttpURLConstant.baseHttpURL.getRegisterSMSV3(), params, new BaseCallback() {
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
                    if (!TextUtils.isEmpty(statusCode) && statusCode.equals("-212401")) {
                        if (baseView != null) {
                            baseView.sendSMSFailure(statusCode);
                        }
                    } else {
                        baseView.sendSMSFailure(message);
                    }
                }
            });


        }
    }


}
