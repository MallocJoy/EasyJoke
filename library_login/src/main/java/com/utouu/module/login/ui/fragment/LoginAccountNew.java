package com.utouu.module.login.ui.fragment;

import android.content.Context;

import com.utouu.android.commons.constants.DevHttpURL;
import com.utouu.android.commons.constants.HttpURLConstant;
import com.utouu.android.commons.constants.LiveHttpURL;
import com.utouu.android.commons.constants.TestHttpURL;
import com.utouu.android.commons.entity.LoginParams;
import com.utouu.android.commons.http.BaseCallback;
import com.utouu.android.commons.http.UtouuHttpUtils;
import com.utouu.android.commons.presenter.model.ILoginAccount;
import com.utouu.android.commons.utils.GenerateSignDemo;

import java.util.HashMap;

/**
 * Created by chenxin on 2017/2/20.
 * Function:
 * Desc:
 */

public class LoginAccountNew implements ILoginAccount {
    @Override
    public void getTGT(Context context, LoginParams loginParams, final Callback callback) {
        final boolean isOpenAccount = loginParams.isOpenAccount();
        final String account = loginParams.getAccount();
        final String password = loginParams.getPassword();

        String requestUrl;

        HashMap<String, String> basicParams = new HashMap<>();
        basicParams.put("device_type", loginParams.getDeviceType());
        basicParams.put("device_udid", loginParams.getDeviceUdid());
        basicParams.put("version", loginParams.getVersion());
        basicParams.put("device_token", loginParams.getDeviceToken());
        basicParams.put("app_name", loginParams.getAppName());
        basicParams.put("confirm_change", String.valueOf(loginParams.isConfirmChange()));
        basicParams.put("confirm_code", loginParams.getConfirmCode());


        HashMap<String, String> params = new HashMap<>();
        if (isOpenAccount) {
            params.put("open_id", loginParams.getOpenId());
            params.put("open_type", String.valueOf(loginParams.getOpenType()));

            long currentTimeMillis = System.currentTimeMillis();
            String sign = GenerateSignDemo.generateCommonSign(currentTimeMillis, params);
            params.put("time", String.valueOf(currentTimeMillis));
            params.put("sign", sign);

            params.putAll(basicParams);
            params.put("accept_agreemt", String.valueOf(loginParams.isAcceptAgreement()));
            params.put("access_token", loginParams.getAccessToken());
            params.put("union_id", loginParams.getUnionId());
            params.put("push_platform", String.valueOf(loginParams.getPushPlatform()));

            requestUrl = HttpURLConstant.baseHttpURL.getOpenAccountTickets();
        } else {
            params.put("username", account);
            params.put("password", password);
            params.putAll(basicParams);

            long currentTimeMillis = System.currentTimeMillis();
            String sign = GenerateSignDemo.generateCommonSign(currentTimeMillis, params);
            params.put("time", String.valueOf(currentTimeMillis));
            params.put("sign", sign);

            params.put("accept_agreemt", String.valueOf(loginParams.isAcceptAgreement()));
            params.put("push_platform", String.valueOf(loginParams.getPushPlatform()));
            if (HttpURLConstant.baseHttpURL == TestHttpURL.getInstance()) {
                params.put("utouuAppId", "zTzfMiAmR-Kp18PydSlb1A");
                params.put("service", "http://app.test.xunions.cn/v2/");//192.168.121.130:30455
            } else if (HttpURLConstant.baseHttpURL == DevHttpURL.getInstance()) {
                params.put("utouuAppId", "YLgk67euQZyJR5hWYxtmRQ");
                params.put("service", "http://app.dev.xunions.cn/v2/");
            } else if (HttpURLConstant.baseHttpURL == LiveHttpURL.getInstance()) {
                params.put("utouuAppId", "zTzfMiAmR-Kp18PydSlb1A");//修改appid
                params.put("service", "http://app.xunions.cn/v2/");
            }
            requestUrl = HttpURLConstant.baseHttpURL.getTickets();
        }

        UtouuHttpUtils.loadData(context, requestUrl, params, new BaseCallback() {
            @Override
            public void success(String message) {
                callback.success(account, password, message);
            }

            @Override
            public void failure(String message) {
                callback.failure(message);
            }

            @Override
            public void failure(String statusCode, String message) {
                callback.failure(statusCode, message);
            }
        });
    }
}
