package com.utouu.module.login.presenter;

import android.content.Context;

import com.utouu.android.commons.presenter.BasePresenter;
import com.utouu.android.commons.presenter.model.IBindAccountAction;
import com.utouu.android.commons.presenter.model.impl.BindAccountAction;
import com.utouu.module.login.presenter.view.IBindAccountView;

/**
 * 绑定平台账号
 * User: yang
 * Date: 2016-03-03 20:14
 */
public class BindAccountPresenter extends BasePresenter<IBindAccountView> {

    private IBindAccountAction bindAccountAction;

    public BindAccountPresenter(IBindAccountView baseView) {
        super(baseView);
        this.bindAccountAction = new BindAccountAction();
    }

    /**
     * 绑定账号
     *
     * @param context
     * @param account     账号
     * @param password    密码
     * @param openId      第三方openId
     * @param openType    1:QQ 2:微信
     * @param deviceType  设备登录类型
     * @param accessToken 令牌
     */
    public void bindAccount(Context context, String account, String password, String openId, String openType, int deviceType,
            String accessToken) {
        if (baseView != null) {
            bindAccountAction.bindAccount(context, account, password, openId, openType, deviceType, accessToken,
                    new IBindAccountAction.BindAccountCallback() {
                        @Override
                        public void bindSuccess(String content) {
                            if (baseView != null) {
                                baseView.success(content);
                            }
                        }

                        @Override
                        public void bindFailure(String message) {
                            if (baseView != null) {
                                baseView.failure(message);
                            }
                        }

                        @Override
                        public void bindFailure(String errorCode, String message) {
                            if (baseView != null) {
                                baseView.failure(errorCode, message);
                            }
                        }
                    });
        }
    }
}
