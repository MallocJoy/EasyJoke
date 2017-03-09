package com.utouu.module.login.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.utouu.android.commons.constants.HttpURLConstant;
import com.utouu.android.commons.http.BaseCallback;
import com.utouu.android.commons.http.CheckLoginCallback;
import com.utouu.android.commons.http.UtouuHttpUtils;
import com.utouu.android.commons.presenter.model.impl.SendSMS;
import com.utouu.android.commons.utils.CheckParamsUtils;
import com.utouu.android.commons.utils.StringUtils;
import com.utouu.android.commons.utils.ToastUtils;
import com.utouu.module.login.R;
import com.utouu.module.login.presenter.BindAccountPresenter;
import com.utouu.module.login.presenter.RegisterPresenter;
import com.utouu.module.login.presenter.view.IBindAccountView;
import com.utouu.module.login.presenter.view.IRegisterView;
import com.utouu.module.login.widget.BaseDialog;
import com.utouu.module.login.widget.BaseTextWatcher;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.UUID;

import static android.R.attr.type;
import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * User: Administrator
 * Date: 2016-05-24 17:58
 */
public class AccountRegisterActivity extends BaseActivity implements IRegisterView, IBindAccountView {

    private static final String ARG_BIND = "nextBind";

    private ImageView verificationImg, pwdEyeImageView;
    private String key, smsKey;
    private EditText mobileEditText, passwordEditText, cipherEditText;
    private Button registerButton;

    private TextView countdownTextView;

    private static int waitCount = 60;
    private CountDownTimer countDownTimer;
    private EditText mobileVerifyEditText;

    private Bundle dataBundle;
    private String deviceType = "";
    private boolean nextBind = false;

    private RegisterPresenter registerAccountPresenter;
    private BindAccountPresenter bindAccountPresenter;

    private Dialog smsVerifyCodeDialog;
    private ImageView dialogImageView;
    private LinearLayout mobileVerifyLinearLayout;
    private SendSMS sendSMS;


    public static void startForResult(Activity context, int openType, String openId, String unionId, String accessToken,
                                      String deviceType, int requestCode) {
        Intent intent = new Intent(context, AccountRegisterActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString(AccountBindActivity.KEY_PARAMS_OPEN_ID, openId);
        bundle.putString(AccountBindActivity.KEY_PARAMS_UNION_ID, unionId);
        bundle.putInt(AccountBindActivity.KEY_PARAMS_OPEN_TYPE, openType);
        bundle.putString(AccountBindActivity.KEY_PARAMS_ACCESS_TOKEN, accessToken);
        bundle.putString(BaseLoginActivity.ARG_DEVICE_TYPE, deviceType);
        bundle.putBoolean(ARG_BIND, true);
        intent.putExtras(bundle);

        context.startActivityForResult(intent, requestCode);
    }

    public static void startForResult(Activity context, String deviceType, int requestCode) {
        Intent intent = new Intent(context, AccountRegisterActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(BaseLoginActivity.ARG_DEVICE_TYPE, deviceType);
        bundle.putBoolean(ARG_BIND, false);
        context.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_register);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);

        if (getIntent() != null) {
            dataBundle = getIntent().getExtras();
            if (dataBundle != null) {
                deviceType = getIntent().getStringExtra(BaseLoginActivity.ARG_DEVICE_TYPE);
                nextBind = dataBundle.getBoolean(ARG_BIND, false);
            }
        }

        setTitleBar(getString(R.string.commons_title_register));

        sendSMS = new SendSMS();
        registerAccountPresenter = new RegisterPresenter(this);
        bindAccountPresenter = new BindAccountPresenter(this);

        initViews();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getImageVify();
    }

    /**
     * 获取注册时需要的图形验证码
     */
    private void getImageVify() {
        key = UUID.randomUUID().toString();
        registerAccountPresenter.getImageVify(this, deviceType, key);
    }

    /**
     * 获取发送短信验证码时需要的图形验证码
     */
    private void getSmsImageVify() {
        smsKey = UUID.randomUUID().toString();
        registerAccountPresenter.getSmsImageVify(this, deviceType, smsKey);
    }

    private void initViews() {

        mobileVerifyLinearLayout = (LinearLayout) findViewById(R.id.mobile_verify_linearLayout);
//        mobileVerifyLinearLayout.setVisibility(View.GONE);

        verificationImg = (ImageView) findViewById(R.id.verify_imageView);//验证码图片
        mobileEditText = (EditText) findViewById(R.id.mobile_editText);
        mobileEditText.addTextChangedListener(new BaseTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (registerButton != null) {
                    registerButton.setEnabled(StringUtils.isNotBlank(charSequence));
                }
            }
        });
        passwordEditText = (EditText) findViewById(R.id.password_editText);
        pwdEyeImageView = (ImageView) findViewById(R.id.show_password_imageView);//显示隐藏密码
        cipherEditText = (EditText) findViewById(R.id.cipher_editText);
        registerButton = (Button) findViewById(R.id.register_submit_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        verificationImg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getImageVify();
            }
        });
        pwdEyeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOrHidePassword(passwordEditText, pwdEyeImageView);
            }
        });

        mobileVerifyEditText = (EditText) findViewById(R.id.mobile_verify_editText);

        countdownTextView = (TextView) findViewById(R.id.countdown_textView);
        setCountdownTextView(getString(R.string.commons_sms_get));

        countDownTimer = new CountDownTimer(1000 * waitCount, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (null != countdownTextView) {
                    countdownTextView.setText(getString(R.string.commons_countdown_hint, millisUntilFinished / 1000));
                    if (countdownTextView != null) {
                        countdownTextView.setClickable(false);
                    }
                }
            }

            @Override
            public void onFinish() {
                setCountdownTextView(getString(R.string.commons_sms_retry));
                if (countdownTextView != null) {
                    countdownTextView.setClickable(true);
                }
            }
        };


        TextView agreementTextView = (TextView) findViewById(R.id.agreement_textView);
        if (agreementTextView != null) {
            agreementTextView.setText(Html.fromHtml(getString(R.string.commons_register_agreement2)));
            agreementTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //WebActivity.start(v.getContext(), getString(R.string.commons_register_agreement1),"https://passport.dev.utouu.com/xweed-agreement.html");
                    WebActivity.start(v.getContext(), "创盟用户服务协议","http://www.dev.xunions.cn/v1/h5/rules");

                }
            });
        }
    }



    private void setCountdownTextView(CharSequence text) {
        if (countdownTextView != null) {
            countdownTextView.setText(text);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                countdownTextView.setTextColor(getColor(R.color.commons_text_color2));
            } else {
                countdownTextView.setTextColor(getResources().getColor(R.color.commons_text_color2));
            }
            countdownTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    getSMS();

                }
            });
        }
    }

    private void register() {
        if (validate()) {
            String tempMobile = StringUtils.trim(mobileEditText.getText().toString());
            String tempPassword = StringUtils.trim(passwordEditText.getText().toString());
            String tempCipher = StringUtils.trim(cipherEditText.getText().toString());
//            String tempVCodeKey = key;

            String tempSMSCode = null;
            if (mobileVerifyLinearLayout != null && mobileVerifyLinearLayout.getVisibility() == View.VISIBLE) {
                tempSMSCode = mobileVerifyEditText.getText().toString().trim();
            }

            showProgress();

            registerAccountPresenter.register(this,tempMobile, tempPassword,"7",tempCipher,tempSMSCode);
//            registerAccountPresenter
//                    .register(this, tempMobile, tempPassword, "7", tempCipher, tempSMSCode, tempVCodeKey, tempVCode);
        }
    }

    /**
     * 注册校验
     *
     * @return true:校验通过/false:校验失败
     */
    private boolean validate() {

        CharSequence tempMobile = mobileEditText.getText();
        if (StringUtils.isBlank(tempMobile)) {
            ToastUtils.showShortToast(this, getString(R.string.commons_account_validate3));
            return false;
        }
        if (!CheckParamsUtils.checkMobile(tempMobile)) {
            ToastUtils.showShortToast(this, getString(R.string.commons_account_validate2));
            return false;
        }

        CharSequence tempPassword = passwordEditText.getText();
        if (StringUtils.isBlank(tempPassword)) {
            ToastUtils.showShortToast(this, getString(R.string.commons_password_validate1));
            return false;
        }
        if (!CheckParamsUtils.checkPass(tempPassword)) {
            ToastUtils.showShortToast(this, getString(R.string.commons_password_validate2));
            return false;
        }

//        CharSequence tempCode = codeEdit.getText();
//        if (StringUtils.isBlank(tempCode)) {
//            ToastUtils.showShortToast(this, getString(R.string.commons_verify_validate));
//        }

        if (mobileVerifyLinearLayout != null && mobileVerifyLinearLayout.getVisibility() == View.VISIBLE) {
            CharSequence mobileVerify = mobileVerifyEditText.getText();
            if (StringUtils.isBlank(mobileVerify)) {
                ToastUtils.showShortToast(this, getString(R.string.commons_sms_validate));
                return false;
            }
        }
        return true;
    }

    /**
     * 获取短信验证码
     * User: yang
     * Date: 2016-03-03 22:56
     */
    private void getSMS() {

        final String mobile = mobileEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(mobile)) {
            ToastUtils.showShortToast(this, getString(R.string.commons_account_validate1));
            return;
        }

        if (TextUtils.isEmpty(password)) {
            ToastUtils.showShortToast(this, getString(R.string.commons_password_validate1));
            return;
        }

        sendSMS.sendSMS(AccountRegisterActivity.this, mobile, 3, "", "", getIMEI(AccountRegisterActivity.this), new BaseCallback() {
            @Override
            public void success(String message) {
                if (countDownTimer != null) {
                    countDownTimer.start();
                }
                ToastUtils.showLongToast(AccountRegisterActivity.this, message);
            }

            @Override
            public void failure(String message) {
                ToastUtils.showLongToast(AccountRegisterActivity.this, message);
                if (countdownTextView != null) {
                    countdownTextView.setClickable(true);
                }
            }

            @Override
            public void failure(String statusCode, String message) {

                if (!TextUtils.isEmpty(statusCode) && statusCode.equals("-212401")) {
                    getCodeImage();//图片验证码
                } else {
                    ToastUtils.showShortToast(AccountRegisterActivity.this, message);
                }

                if (countdownTextView != null) {
                    countdownTextView.setClickable(true);
                }
            }
        });

    }

    /**
     * 图片验证码
     */
    private void getCodeImage() {

        smsVerifyCodeDialog = new Dialog(this, R.style.commons_baseDialog1);
        View contentView = LayoutInflater.from(this).inflate(R.layout.base_dialog_edit, null);
        Button dialogNegativeButton = (Button) contentView.findViewById(R.id.negativeButton);
        Button dialogPositiveButton = (Button) contentView.findViewById(R.id.positiveButton);
        final EditText dialogEditText = (EditText) contentView.findViewById(R.id.verification_code_reg_edit);
        dialogImageView = (ImageView) contentView.findViewById(R.id.verification_reg_img);
        dialogImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSmsImageVify();
            }
        });
        smsVerifyCodeDialog.setContentView(contentView);
        smsVerifyCodeDialog.setCanceledOnTouchOutside(false);
        smsVerifyCodeDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                getSmsImageVify();
            }
        });


        smsVerifyCodeDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                UtouuHttpUtils.cancelRequests(AccountRegisterActivity.this);
            }
        });


        dialogNegativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UtouuHttpUtils.cancelRequests(AccountRegisterActivity.this);
                smsVerifyCodeDialog.dismiss();
            }
        });

        //确定
        dialogPositiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String content = dialogEditText.getText().toString();

                if (TextUtils.isEmpty(content)) {
                    ToastUtils.showShortToast(AccountRegisterActivity.this, getString(R.string.commons_verify_validate));
                    return;
                }

                countdownTextView.setClickable(false);
                ToastUtils.showShortToast(AccountRegisterActivity.this, "短信验证码获取中...");

                String mobile = mobileEditText.getText().toString().trim();

                sendSMS.sendSMS(AccountRegisterActivity.this, mobile, 3, smsKey, content, getIMEI(AccountRegisterActivity.this), new BaseCallback() {
                    @Override
                    public void success(String message) {
                        if (countDownTimer != null) {
                            countDownTimer.start();
                        }
                        ToastUtils.showLongToast(AccountRegisterActivity.this, message);
                    }

                    @Override
                    public void failure(String message) {
                        ToastUtils.showLongToast(AccountRegisterActivity.this, message);
                        if (countdownTextView != null) {
                            countdownTextView.setClickable(true);
                        }
                    }

                    @Override
                    public void failure(String statusCode, String message) {

                        if (!TextUtils.isEmpty(statusCode) && statusCode.equals("-212401")) {
                            getCodeImage();//图片验证码
                        } else {
                            ToastUtils.showShortToast(AccountRegisterActivity.this, message);
                        }

                        if (countdownTextView != null) {
                            countdownTextView.setClickable(true);
                        }

                    }
                });

                smsVerifyCodeDialog.dismiss();


            }
        });
        smsVerifyCodeDialog.show();

    }


    /**
     * 获取设备IMEI号
     *
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {

        String imei = "";
        try {
            // 获取设备信息
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (null != tm) {
                imei = tm.getDeviceId();
            }
            if (null == imei) {
                imei = "";
            }

            // getLine1Number 手机号
            // getSimSerialNumber SIM卡序号
            // getSubscriberId IMSI
        } catch (Exception e) {
            ToastUtils.showLongToast(context, "获取设备信息出错.");
        }
        return imei;
    }

    private void showOrHidePassword(EditText passwordEditText, ImageView eyeImageView) {
        if (null == passwordEditText || null == eyeImageView) {
            return;
        }

        final int index = passwordEditText.getSelectionEnd();//得到光标位置

        TransformationMethod mTransformationMethod = passwordEditText.getTransformationMethod();
        if (mTransformationMethod instanceof PasswordTransformationMethod) {//如果是密文
            eyeImageView.setImageResource(R.mipmap.icon_show_pass_checked);
            passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else if (mTransformationMethod instanceof HideReturnsTransformationMethod) {
            eyeImageView.setImageResource(R.mipmap.icon_show_pass_normal);
            passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        passwordEditText.setSelection(index);//回复光标位置
    }

    @Override
    public void registerSuccess(String message) {
        if (isFinishing()) {
            return;
        }

        new BaseDialog.Builder(this).setMessage(TextUtils.isEmpty(message) ? "注册成功." : message).setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String tempMobile = StringUtils.trim(mobileEditText.getText().toString());

                        if (nextBind) {
                            String openId = dataBundle.getString(AccountBindActivity.KEY_PARAMS_OPEN_ID);
                            String accessToken = dataBundle.getString(AccountBindActivity.KEY_PARAMS_ACCESS_TOKEN);
                            int openType = dataBundle.getInt(AccountBindActivity.KEY_PARAMS_OPEN_TYPE, 1);
                            String password = StringUtils.trim(passwordEditText.getText().toString());

                            showProgress("关联账号中...");

                            bindAccountPresenter.bindAccount(AccountRegisterActivity.this, tempMobile, password, openId,
                                    Integer.toString(openType), 1, accessToken);
                        } else {
                            dismissProgress();
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("mobile", tempMobile);
                            setResult(RESULT_OK, resultIntent);
                            finish();
                        }
                    }
                }).show();
    }

    @Override
    public void registerFailure(String message) {
        if (isFinishing()) {
            return;
        }
        dismissProgress();
        ToastUtils.showShortToast(this, message);
    }

    @Override
    public void registerFailure(String errorCode, String message) {
        registerFailure(message);
    }

    @Override
    public void smsImageVifySuccess(Bitmap bitmap) {
        if (bitmap != null && smsVerifyCodeDialog != null && smsVerifyCodeDialog.isShowing() && dialogImageView != null) {
            dialogImageView.setImageBitmap(bitmap);
        }
    }

    @Override
    public void smsImageVifyFailure(String message) {
        ToastUtils.showShortToast(this, message);
    }

    @Override
    public void sendSMSSuccess(String message) {
        ToastUtils.showShortToast(this, message);

        if (countdownTextView != null) {
            countdownTextView.setOnClickListener(null);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                countdownTextView.setTextColor(getColor(R.color.commons_text_hint_color));
            } else {
                countdownTextView.setTextColor(getResources().getColor(R.color.commons_text_hint_color));
            }
            if (countDownTimer != null) {
                countDownTimer.start();
            }
        }
    }

    @Override
    public void sendSMSFailure(String message) {
        ToastUtils.showShortToast(this, message);
    }

    @Override
    public void verifySuccess(Bitmap bitmap) {
        if (null != bitmap && null != verificationImg) {
            verificationImg.setScaleType(ImageView.ScaleType.FIT_XY);
            verificationImg.setImageBitmap(bitmap);
        }
    }

    @Override
    public void verifyFailure(String message) {
        ToastUtils.showShortToast(this, message);
    }

    @Override
    public void success(String content) {
        dismissProgress();

        ToastUtils.showLongToast(this, content);
        Intent intent = new Intent();
        intent.putExtras(dataBundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void failure(String message) {
        dismissProgress();
        ToastUtils.showLongToast(this, message);
        finish();
    }

    @Override
    public void failure(String errorCode, String message) {
        failure(message);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

}
