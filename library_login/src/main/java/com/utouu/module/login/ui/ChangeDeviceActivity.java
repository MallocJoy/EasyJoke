package com.utouu.module.login.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.utouu.android.commons.utils.StringUtils;
import com.utouu.android.commons.utils.ToastUtils;
import com.utouu.module.login.R;
import com.utouu.module.login.presenter.RenewDevicesPresenter;
import com.utouu.module.login.presenter.view.IRenewDevicesView;
import com.utouu.module.login.widget.BaseTextWatcher;

/**
 * Created by yang on 16/2/25.
 */
public class ChangeDeviceActivity extends BaseActivity implements IRenewDevicesView {

    private TextView countdownTextView;

    private EditText mobileVerifyEditText;
    private Button submitButton;

    private CountDownTimer countDownTimer;

    private int waitCount = 60;

    private RenewDevicesPresenter renewDevicesPresenter;
    private String mobile;

    public static void startForResult(Activity activity, String mobile, int requestCode) {
        Intent intent = new Intent(activity, ChangeDeviceActivity.class);
        intent.putExtra("mobile", mobile);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_device);

        if (getIntent() != null) {
            mobile = getIntent().getStringExtra("mobile");
        }

        setTitleBar(getString(R.string.commons_title_changeDevices));

        countdownTextView = (TextView)findViewById(R.id.countdown_textView);

        mobileVerifyEditText = (EditText)findViewById(R.id.mobile_verify_editText);
        if (mobileVerifyEditText != null) {
            mobileVerifyEditText.addTextChangedListener(new BaseTextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (submitButton != null) {
                        submitButton.setEnabled(!TextUtils.isEmpty(s));
                    }
                }
            });
        }

        submitButton = (Button)findViewById(R.id.submit_button);
        if (submitButton != null) {
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String smsContent = getMobileVerifyEditText().getText().toString().trim();
                    if (smsContent.length() == 0) {
                        ToastUtils.showShortToast(ChangeDeviceActivity.this, getString(R.string.commons_sms_validate));
                        return;
                    }
                    Intent intent = new Intent();
                    intent.putExtra("verifyValue", smsContent);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }


        countDownTimer = new CountDownTimer(1000 * waitCount, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (null != countdownTextView) {
                    countdownTextView.setText(getString(R.string.commons_countdown_hint, (millisUntilFinished / 1000)));
                }
            }

            @Override
            public void onFinish() {
                if (null != countdownTextView) {
                    countdownTextView.setText(getString(R.string.commons_sms_retry));
                    countdownTextView.setTextColor(getResources().getColor(R.color.commons_text_color2));
                    countdownTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            repeatSendSMS();
                        }
                    });
                }
            }
        };
        countDownTimer.start();

        renewDevicesPresenter = new RenewDevicesPresenter(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    /**
     * 重新获取验证码
     */
    private void repeatSendSMS() {
        if (StringUtils.isNotBlank(mobile)) {
            renewDevicesPresenter.sendSMS(this, mobile);
        } else {
            ToastUtils.showShortToast(this, "未获取到手机号码！");
        }
    }

    private EditText getMobileVerifyEditText() {
        return (EditText)findViewById(R.id.mobile_verify_editText);
    }

    @Override
    public void sendSMSSuccess(String message) {
        countdownTextView.setOnClickListener(null);
        countdownTextView.setTextColor(getResources().getColor(R.color.commons_text_hint_color));
        countDownTimer.start();

        ToastUtils.showShortToast(this, message);
    }

    @Override
    public void sendSMSFailure(String message) {
        ToastUtils.showLongToast(this, message);
    }
}
