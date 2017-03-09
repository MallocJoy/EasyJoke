package com.utouu.module.login.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.utouu.android.commons.constants.HttpURLConstant;
import com.utouu.android.commons.entity.LoginParams;
import com.utouu.android.commons.utils.CheckParamsUtils;
import com.utouu.android.commons.utils.DeviceUtils;
import com.utouu.android.commons.utils.StringUtils;
import com.utouu.android.commons.utils.ToastUtils;
import com.utouu.module.login.R;
import com.utouu.module.login.adapter.AccountNumberAdapter;
import com.utouu.module.login.bean.AccountNumberBean;
import com.utouu.module.login.presenter.LoginPresenter;
import com.utouu.module.login.presenter.view.ILoginView;
import com.utouu.module.login.widget.BaseDialog;
import com.utouu.module.login.widget.BaseTextWatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class BaseLoginActivity extends BaseActivity implements ILoginView {

    public static final int REQUEST_CODE_REGISTER = 0x11;
    public static final int REQUEST_CODE_RETRIEVE = 0x22;
    public static final int REQUEST_CODE_CHANGE_DEVICE = 0x33;
    public static final int REQUEST_CODE_BIND_ACCOUNT = 0x44;
    public static final int REQUEST_CODE_REGISTER_ACCOUNT_AND_BIND = 0x55;

    public static final String ARG_DEVICE_TYPE = "deviceType";
    private static final String ARG_DEVICE_UDID = "deviceUdid";
    private static final String ARG_DEVICE_TOKEN = "deviceToken";
    private static final String ARG_APP_NAME = "appName";
    private static final String ARG_VERSION_NAME = "versionName";
    private static final String ARG_OTHER_LOGIN = "otherLogin";
    private static final String ARG_PUSH_PLATFORM = "pushPlatform";

    private ImageView userHeadImageView;
    private AutoCompleteTextView mobileAutoCompleteTextView;
    private ImageView mobileClearImageView;
    private ListView mobileListView;
    private EditText passwordEditText;
    private ImageView showPasswordImageView;
    private Button loginButton;

//    private ImageView showAccountsImageView;

    private List<AccountNumberBean> accountNumberBeans = new ArrayList<>();

    private LoginPresenter loginPresenter;

    private UMShareAPI umShareAPI;
    private UMAuthListener umAuthListener;

    private String deviceType;
    private String deviceUdid;
    private String deviceToken;
    private String appName;
    private String versionName;
    private boolean showOtherLogin;
    private int pushPlatform;

    private Dialog changeDeviceDialog;
    private Dialog bindAccountDialog;
    private Dialog agreeMentDialog;

    ///////////////////////////////////////////////////////////////////////////
    // 第三方登录参数
    private int openType = -1; // 1:QQ  2:微信
    private String openId;
    private String accessToken;
    private String unionId;
    ///////////////////////////////////////////////////////////////////////////

    private LoginParams lastloginParams;

    public static void start(Context context, Class<? extends BaseLoginActivity> toActivity, String deviceType, String deviceUdid,
                             String deviceToken, String appName, String versionName, boolean showOtherLogin) {
        start(context, toActivity, deviceType, deviceUdid, deviceToken, appName, versionName, showOtherLogin, 2);
    }

    /**
     * 启动
     *
     * @param context
     * @param toActivity
     * @param deviceType     APP登录类型
     * @param deviceUdid     udid
     * @param deviceToken    推送token
     * @param appName        应用名(英文)
     * @param versionName    版本号
     * @param showOtherLogin 是否显示第三方登录
     * @param pushPlatform 推送类型：1.信鸽推送、2.阿里云推送
     */
    public static void start(Context context, Class<? extends BaseLoginActivity> toActivity, String deviceType, String deviceUdid,
                             String deviceToken, String appName, String versionName, boolean showOtherLogin, int pushPlatform) {
        Intent intent = new Intent(context, toActivity);
        intent.putExtra(ARG_DEVICE_TYPE, deviceType);
        intent.putExtra(ARG_DEVICE_UDID, deviceUdid);
        intent.putExtra(ARG_DEVICE_TOKEN, deviceToken);
        intent.putExtra(ARG_APP_NAME, appName);
        intent.putExtra(ARG_VERSION_NAME, versionName);
        intent.putExtra(ARG_OTHER_LOGIN, showOtherLogin);
        intent.putExtra(ARG_PUSH_PLATFORM, pushPlatform);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (getIntent() != null) {
            deviceType = getIntent().getStringExtra(ARG_DEVICE_TYPE);
            deviceUdid = getIntent().getStringExtra(ARG_DEVICE_UDID);
            deviceToken = getIntent().getStringExtra(ARG_DEVICE_TOKEN);
            appName = getIntent().getStringExtra(ARG_APP_NAME);
            versionName = getIntent().getStringExtra(ARG_VERSION_NAME);
            showOtherLogin = getIntent().getBooleanExtra(ARG_OTHER_LOGIN, false);
            pushPlatform = getIntent().getIntExtra(ARG_PUSH_PLATFORM, 2);
        }

        loginPresenter = new LoginPresenter(this);

        View tempView1 = findViewById(R.id.temp_frameLayout1);
        if (tempView1 != null) {
            tempView1.setVisibility(showOtherLogin ? View.VISIBLE : View.GONE);
        }
        View tempView2 = findViewById(R.id.temp_linearLayout2);
        if (tempView2 != null) {
            tempView2.setVisibility(showOtherLogin ? View.VISIBLE : View.GONE);
        }

        userHeadImageView = (ImageView) findViewById(R.id.user_head_imageView);
        mobileAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.mobile_autocCompleteTextView);
        mobileClearImageView = (ImageView) findViewById(R.id.mobile_clear_imageView);
//        showAccountsImageView = (ImageView)findViewById(R.id.show_accounts_imageView);
//        showAccountsImageView.setTag("close");
//        showAccountsImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String flag = String.valueOf(v.getTag());
//                boolean isClose = flag.equals("close");
//                if (mobileListView != null) {
//                    if (mobileListView.getAdapter() == null || mobileListView.getAdapter().getCount() == 0) {
//                        mobileListView.setVisibility(View.GONE);
//                    } else {
//                        mobileListView.setVisibility(isClose ? View.VISIBLE : View.GONE);
//                    }
//                }
//                v.setTag(isClose ? "open" : "close");
//                showAccountsImageView.setImageResource(isClose ? R.mipmap.icon_arrow_top : R.mipmap.icon_arrow_bottom);
//            }
//        });

        mobileListView = (ListView) findViewById(R.id.mobile_listView);
        mobileListView.setVisibility(View.GONE);
        if (accountNumberBeans != null) {
            if (accountNumberBeans.size() > 0) {
                AccountNumberAdapter accountNumberAdapter = new AccountNumberAdapter(accountNumberBeans);
                accountNumberAdapter.setItemCallback(new AccountNumberAdapter.ItemCallback() {
                    @Override
                    public void clickItem(AccountNumberBean accountNumberBean) {
                        if (mobileAutoCompleteTextView != null) {
                            String tempLoginName = accountNumberBean.loginName;
                            mobileAutoCompleteTextView.setText(tempLoginName);
                            mobileAutoCompleteTextView.setSelection(tempLoginName.length());
                        }
//                        if (showAccountsImageView != null) {
//                            showAccountsImageView.performClick();
//                        }
                    }

                    @Override
                    public void clearItem(AccountNumberBean userBean) {
                        if (mobileListView != null && mobileListView.getAdapter().getCount() == 0) {
                            mobileListView.setVisibility(View.GONE);
//                            showAccountsImageView.setImageResource(R.mipmap.icon_arrow_bottom);
//                            showAccountsImageView.setTag("close");
                        }
                    }
                });
                mobileListView.setAdapter(accountNumberAdapter);
            }
        }

        passwordEditText = (EditText) findViewById(R.id.password_editText);
        showPasswordImageView = (ImageView) findViewById(R.id.show_password_imageView);

        mobileAutoCompleteTextView.addTextChangedListener(new BaseTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                super.onTextChanged(charSequence, start, before, count);
                final boolean isMobileNull = StringUtils.isBlank(charSequence);
                if (!isMobileNull) {
                    if (null != mobileClearImageView && mobileClearImageView
                            .getVisibility() != View.VISIBLE && mobileAutoCompleteTextView.hasFocus()) {
                        mobileClearImageView.setVisibility(View.VISIBLE);
                    }
                    if (accountNumberBeans != null && accountNumberBeans.size() > 0) {
                        String headUrl = StringUtils.EMPTY;

                        AccountNumberBean accountNumberBean;
                        for (int i = 0; i < accountNumberBeans.size(); i++) {
                            accountNumberBean = accountNumberBeans.get(i);
                            if (charSequence.toString().equals(accountNumberBean.loginName)) {
                                headUrl = accountNumberBean.headUrl;
                            }
                        }
                        if (userHeadImageView != null) {
                            if (StringUtils.isNotBlank(headUrl)) {
                                Picasso.with(BaseLoginActivity.this).load(headUrl).placeholder(R.mipmap.head_default)
                                        .error(R.mipmap.head_default).into(userHeadImageView);
                            } else {
                                userHeadImageView.setImageResource(R.mipmap.head_default);
                            }
                        }
                    }
                } else {
                    if (null != mobileClearImageView && mobileClearImageView.getVisibility() != View.GONE) {
                        mobileClearImageView.setVisibility(View.GONE);
                    }
                    if (userHeadImageView != null) {
                        userHeadImageView.setImageResource(R.mipmap.head_default);
                    }
                }
                if (null != loginButton) {
                    loginButton.setEnabled(!isMobileNull);
                }
                if (passwordEditText != null) {
                    passwordEditText.setText(StringUtils.EMPTY);
                }
            }
        });
        mobileAutoCompleteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                final boolean isMobileNull = TextUtils.isEmpty(mobileAutoCompleteTextView.getText());
                if (hasFocus) {
                    if (!isMobileNull) {
                        if (null != mobileClearImageView && mobileClearImageView.getVisibility() != View.VISIBLE) {
                            mobileClearImageView.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    if (null != mobileClearImageView && mobileClearImageView.getVisibility() != View.GONE) {
                        mobileClearImageView.setVisibility(View.GONE);
                    }
                }
            }
        });


        mobileClearImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearMobile();
            }
        });

        TransformationMethod transformationMethod = passwordEditText.getTransformationMethod();
        if (transformationMethod instanceof PasswordTransformationMethod) {
            showPasswordImageView.setImageResource(R.mipmap.icon_show_pass_normal);
        } else if (transformationMethod instanceof HideReturnsTransformationMethod) {
            showPasswordImageView.setImageResource(R.mipmap.icon_show_pass_checked);
        }

        showPasswordImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int index = passwordEditText.getSelectionEnd();
                TransformationMethod transformationMethod = passwordEditText.getTransformationMethod();
                if (transformationMethod instanceof PasswordTransformationMethod) {
                    showPasswordImageView.setImageResource(R.mipmap.icon_show_pass_checked);
                    passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else if (transformationMethod instanceof HideReturnsTransformationMethod) {
                    showPasswordImageView.setImageResource(R.mipmap.icon_show_pass_normal);
                    passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                passwordEditText.setSelection(index);
            }
        });

        TextView registerTextView = (TextView) findViewById(R.id.register_textView);
        if (null != registerTextView) {
            registerTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doRegister();
                }
            });
        }
        TextView retrieveTextview = (TextView) findViewById(R.id.retrieve_textView);
        if (retrieveTextview != null) {
            retrieveTextview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doRetrieve();
                }
            });
        }

        loginButton = (Button) findViewById(R.id.login_button);
        if (loginButton != null) {
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doLogin();
                }
            });
        }

        //QQ授权登录
        ImageView qqImageView = (ImageView) findViewById(R.id.qq_imageView);
        if (qqImageView != null) {
            qqImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    oauthVerify(SHARE_MEDIA.QQ);
                }
            });
        }

        //WeChat授权登录
        ImageView weixinImageView = (ImageView) findViewById(R.id.weixin_imageView);
        if (weixinImageView != null) {
            weixinImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    oauthVerify(SHARE_MEDIA.WEIXIN);
                }
            });
        }
    }

    /**
     * 跳转注册
     */
    private void doRegister() {
        if (validateDevices()) {
            AccountRegisterActivity.startForResult(BaseLoginActivity.this, deviceType, REQUEST_CODE_REGISTER);
        }
    }

    /**
     * 跳转找回密码
     */
    private void doRetrieve() {
        if (validateDevices()) {
            RetrieveActivity.startForResult(BaseLoginActivity.this, deviceType, REQUEST_CODE_RETRIEVE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (umShareAPI != null) {
            umShareAPI.onActivityResult(requestCode, resultCode, data);
        }
        switch (requestCode) {
            case REQUEST_CODE_CHANGE_DEVICE:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    String verifyValue = data.getStringExtra("verifyValue");
                    changeDeviceLogin(verifyValue);
                }
                break;
            case REQUEST_CODE_RETRIEVE:
            case REQUEST_CODE_REGISTER:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    String tempMobile = data.getStringExtra("mobile");
                    if (mobileAutoCompleteTextView != null && StringUtils.isNotBlank(tempMobile)) {
                        mobileAutoCompleteTextView.setText(tempMobile);
                        if (passwordEditText != null) {
                            passwordEditText.setText("");
                            passwordEditText.setFocusable(true);
                            passwordEditText.requestFocus();
                        }
                    }
                }
                break;
            case REQUEST_CODE_BIND_ACCOUNT:
            case REQUEST_CODE_REGISTER_ACCOUNT_AND_BIND:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null && data.getExtras() != null) {
                        Bundle dataExtras = data.getExtras();
                        String tempOpenId = dataExtras.getString(AccountBindActivity.KEY_PARAMS_OPEN_ID);
                        String tempUnionId = dataExtras.getString(AccountBindActivity.KEY_PARAMS_UNION_ID);
                        String tempAccessToken = dataExtras.getString(AccountBindActivity.KEY_PARAMS_ACCESS_TOKEN);
                        int tempOpenType = dataExtras.getInt(AccountBindActivity.KEY_PARAMS_OPEN_TYPE, 1);
                        otherLogin(tempOpenType, tempOpenId, tempAccessToken, tempUnionId);
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 清空输入框
     */
    private void clearMobile() {
        if (mobileAutoCompleteTextView != null) {
            mobileAutoCompleteTextView.setText(StringUtils.EMPTY);
            mobileAutoCompleteTextView.setFocusable(true);
            mobileAutoCompleteTextView.requestFocus();
        }
        if (passwordEditText != null) {
            passwordEditText.setText(StringUtils.EMPTY);
        }
    }

    /**
     * 启动第三方登录授权
     *
     * @param share_media
     */
    private void oauthVerify(SHARE_MEDIA share_media) {
        if (validateDevices()) {
            if (umShareAPI == null) {
                umShareAPI = UMShareAPI.get(this);
            }
            if (umAuthListener == null) {
                umAuthListener = new CustomUMAuthListener();
            }
            umShareAPI.doOauthVerify(this, share_media, umAuthListener);
        }
    }

    /**
     * 按钮登录
     */
    private void doLogin() {
//        if (!validateDevices()) {
//            return;
//        }

        final String account = mobileAutoCompleteTextView.getText().toString().trim();
        if (StringUtils.isBlank(account)) {
            ToastUtils.showShortToast(this, getString(R.string.commons_account_validate3));
            return;
        }
        if (!CheckParamsUtils.checkMobile(account)) {
            ToastUtils.showShortToast(this, getString(R.string.commons_account_validate2));
            return;
        }

        final String password = passwordEditText.getText().toString();
        if (StringUtils.isBlank(password)) {
            ToastUtils.showShortToast(this, getString(R.string.commons_password_validate1));
            return;
        }

        showProgress();
        lastloginParams = new LoginParams(account, password, deviceType, deviceUdid, deviceToken, appName, versionName, false, "",
                false, false, -1, "", "", "");
        lastloginParams.setPushPlatform(pushPlatform);
        loginPresenter.getTGT(this, lastloginParams);
    }

    /**
     * 第三方登录
     *
     * @param openType    第三方平台类型
     * @param openId      openId
     * @param accessToken accessToken
     * @param unionId     unionId
     */
    private void otherLogin(int openType, String openId, String accessToken, String unionId) {
        if (!validateDevices()) {
            return;
        }

        showProgress();
        lastloginParams = new LoginParams("", "", deviceType, deviceUdid, deviceToken, appName, versionName, false, "", false,
                true, openType, openId, accessToken, unionId);
        loginPresenter.getTGT(this, lastloginParams);
    }

    /**
     * 确认更换设备
     *
     * @param smsVCode 短信验证码
     */
    private void changeDeviceLogin(String smsVCode) {
        if (lastloginParams != null) {
            showProgress();
            lastloginParams.setConfirmChange(true);
            lastloginParams.setConfirmCode(smsVCode);
            loginPresenter.getTGT(this, lastloginParams);
        }
    }

    /**
     * 校验设备
     *
     * @return
     */
    private boolean validateDevices() {
        String imei = DeviceUtils.getIMEI(this);
        if (StringUtils.isBlank(imei)) {
            ToastUtils.showShortToast(this, getString(R.string.commons_imei_validate));
            return false;
        }
        if (DeviceUtils.checkIMEISame(imei) || DeviceUtils.checkVirtualDevice()) {
            ToastUtils.showShortToast(this, getString(R.string.commons_device_validate));
            return false;
        }
        return true;
    }

    @Override
    public void tgtSuccess(String account, String password, String message) {
        if (isFinishing()) {
            return;
        }
//        dismissProgress();
        loginSuccess(account, password, message);
    }

    protected abstract void loginSuccess(String account, String password, String message);

    @Override
    public void tgtFailure(String message) {
        if (isFinishing()) {
            return;
        }
        dismissProgress();
        ToastUtils.showLongToast(this, message);
    }

    @Override
    public void tgtFailure(String errorCode, String message) {
        if (isFinishing()) {
            return;
        }
        dismissProgress();
        switch (errorCode) {
            case "-10"://更换设备操作
                changeDeviceDialog = new BaseDialog.Builder(this).setMessage(message)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ToastUtils.showShortToast(BaseLoginActivity.this, getString(R.string.commons_sms_send));
                                String tempMobile = mobileAutoCompleteTextView.getText().toString().trim();
                                loginPresenter.sendSMS(BaseLoginActivity.this, tempMobile);
                            }
                        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                if (changeDeviceDialog.isShowing()) {
                    changeDeviceDialog.dismiss();
                }
                changeDeviceDialog.show();
                break;
            case "-11"://未绑定联合登录账户
                bindAccountDialog = new BaseDialog.Builder(this).setMessage(message)
                        .setPositiveButton("新注册", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                AccountRegisterActivity
                                        .startForResult(BaseLoginActivity.this, openType, openId, unionId, accessToken, deviceType,
                                                REQUEST_CODE_REGISTER_ACCOUNT_AND_BIND);
                            }
                        }).setNegativeButton("已有账号", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                AccountBindActivity
                                        .startForResult(BaseLoginActivity.this, openType, openId, unionId, accessToken, deviceType,
                                                REQUEST_CODE_BIND_ACCOUNT);
                            }
                        }).create();
                if (bindAccountDialog.isShowing()) {
                    bindAccountDialog.dismiss();
                }
                bindAccountDialog.show();

                break;
            case "-522"://需要确认最新协议
                View contentView = LayoutInflater.from(this).inflate(R.layout.base_dialog2, null);
                TextView contentTextView = (TextView) contentView.findViewById(R.id.content_textView);
                Spanned spanned = Html.fromHtml(getString(R.string.commons_register_agreement3));
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(spanned);
                spannableStringBuilder.setSpan(new ClickableSpan() {
                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setColor(getResources().getColor(R.color.commons_text_color2));
                        ds.setFlags(Paint.UNDERLINE_TEXT_FLAG);
                    }

                    @Override
                    public void onClick(View view) {
                        WebActivity.start(view.getContext(), getString(R.string.commons_register_agreement1),
                                HttpURLConstant.baseHttpURL.getAgrement());
                    }
                }, spanned.toString().indexOf("《") + 1, spanned.toString().indexOf("》"), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                contentTextView.setText(spannableStringBuilder);
                contentTextView.setMovementMethod(LinkMovementMethod.getInstance());
                Button okButton = (Button) contentView.findViewById(R.id.ok_button);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (agreeMentDialog != null && agreeMentDialog.isShowing()) {

                            agreeMentDialog.dismiss();
                        }
                        if (lastloginParams != null) {
                            showProgress();
                            lastloginParams.setAcceptAgreement(true);
                            loginPresenter.getTGT(BaseLoginActivity.this, lastloginParams);
                        }
                    }
                });
                agreeMentDialog = new AlertDialog.Builder(this, R.style.commons_baseDialog2).setCancelable(false)
                        .setView(contentView).create();
                agreeMentDialog.show();
                break;
            default:
                tgtFailure(message);
                break;
        }
    }

    @Override
    public void sendSMSSuccess(String message) {
        ToastUtils.showLongToast(this, message);

        if (changeDeviceDialog != null && changeDeviceDialog.isShowing()) {
            changeDeviceDialog.dismiss();
        }

        ChangeDeviceActivity
                .startForResult(this, mobileAutoCompleteTextView.getText().toString().trim(), REQUEST_CODE_CHANGE_DEVICE);
    }

    @Override
    public void sendSMSFailure(String message) {
        ToastUtils.showLongToast(this, message);
    }

    private class CustomUMAuthListener implements UMAuthListener {

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            if (map != null) {
                openId = map.get("openid");
                accessToken = map.get("access_token");
                unionId = map.get("unionid");
                switch (share_media) {
                    case QQ:
                        openType = 1;
                        otherLogin(openType, openId, accessToken, StringUtils.defaultString(unionId));
                        break;
                    case WEIXIN:
                        openType = 2;
                        otherLogin(openType, openId, accessToken, StringUtils.defaultString(unionId));
                        break;
                }
            }
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            ToastUtils.showShortToast(BaseLoginActivity.this, "授权失败...");
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            ToastUtils.showShortToast(BaseLoginActivity.this, "授权取消...");
        }
    }
}
