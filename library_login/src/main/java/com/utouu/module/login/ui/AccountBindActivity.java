package com.utouu.module.login.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.utouu.android.commons.utils.StringUtils;
import com.utouu.android.commons.utils.ToastUtils;
import com.utouu.module.login.R;
import com.utouu.module.login.presenter.BindAccountPresenter;
import com.utouu.module.login.presenter.view.IBindAccountView;
import com.utouu.module.login.widget.BaseTextWatcher;

/**
 * 第三方账号登录绑定平台账号界面
 * User: yang
 * Date: 2016-03-03 19:36
 */
public class AccountBindActivity extends BaseActivity implements IBindAccountView {

    public static final String KEY_PARAMS_OPEN_ID = "openId";
    public static final String KEY_PARAMS_UNION_ID = "unionId";
    public static final String KEY_PARAMS_OPEN_TYPE = "openType";
    public static final String KEY_PARAMS_ACCESS_TOKEN = "accessToken";

    private ImageView showPasswordImageView;
    private Button bindAccountButton;
    private EditText passwordEditText;
    private EditText mobileEditText;

    private Bundle dataBundle;

    private BindAccountPresenter bindAccountPresenter;

    public static void startForResult(Activity activity, int openType, String openId, String unionId, String accessToken,
                                      String deviceType, int requestCode) {
        Intent intent = new Intent(activity, AccountBindActivity.class);
        Bundle bundle = new Bundle();
        Log.e(">>>>>>$$$$", openId + "?????" + unionId + "?????" + openType + "?????" + accessToken + "?????" + deviceType);
        bundle.putString(AccountBindActivity.KEY_PARAMS_OPEN_ID, openId);
        bundle.putString(AccountBindActivity.KEY_PARAMS_UNION_ID, unionId);
        bundle.putInt(AccountBindActivity.KEY_PARAMS_OPEN_TYPE, openType);
        bundle.putString(AccountBindActivity.KEY_PARAMS_ACCESS_TOKEN, accessToken);
        bundle.putString(BaseLoginActivity.ARG_DEVICE_TYPE, deviceType);
        intent.putExtras(bundle);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_bind);

        if (getIntent() != null) {
            dataBundle = getIntent().getExtras();
        }

        setTitleBar(getString(R.string.commons_title_accountbind));

        showPasswordImageView = (ImageView) findViewById(R.id.show_password_imageView);
        bindAccountButton = (Button) findViewById(R.id.bind_account_customButton);
        mobileEditText = (EditText) findViewById(R.id.mobile_editText);
        mobileEditText.addTextChangedListener(new BaseTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                super.onTextChanged(charSequence, start, before, count);
                if (bindAccountButton != null) {
                    bindAccountButton.setEnabled(StringUtils.isNotBlank(charSequence));
                }
            }
        });

        passwordEditText = (EditText) findViewById(R.id.password_editText);

        if (showPasswordImageView != null) {
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
        }

        bindAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bind();
            }
        });

        bindAccountPresenter = new BindAccountPresenter(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bindAccountPresenter != null) {
            bindAccountPresenter.setBaseView(null);
        }
    }

    private void bind() {
        if (bindAccountPresenter != null) {
            String mobile = mobileEditText.getText().toString().trim();
            if (TextUtils.isEmpty(mobile)) {
                ToastUtils.showShortToast(AccountBindActivity.this, R.string.commons_account_validate3);
                return;
            }

            String password = passwordEditText.getText().toString().trim();
            if (TextUtils.isEmpty(password)) {
                ToastUtils.showShortToast(AccountBindActivity.this, R.string.commons_password_validate1);
                return;
            }

            String openId = "", accessToken = "";
            int openType = 1;

            if (dataBundle != null) {
                openId = dataBundle.getString(KEY_PARAMS_OPEN_ID);
                openType = dataBundle.getInt(KEY_PARAMS_OPEN_TYPE, 1);
                accessToken = dataBundle.getString(KEY_PARAMS_ACCESS_TOKEN);
            }

            if (TextUtils.isEmpty(openId)) {
                ToastUtils.showShortToast(AccountBindActivity.this, "未获取到授权登录信息...");
                return;
            }

            showProgress();
            bindAccountPresenter
                    .bindAccount(AccountBindActivity.this, mobile, password, openId, Integer.toString(openType), 1, accessToken);
        }
    }

    public void back(View view) {
        this.finish();
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
    }

    @Override
    public void failure(String errorCode, String message) {
        failure(message);
    }
}
