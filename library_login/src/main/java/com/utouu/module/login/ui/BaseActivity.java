package com.utouu.module.login.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.utouu.android.commons.http.UtouuHttpUtils;
import com.utouu.android.commons.utils.StringUtils;
import com.utouu.module.login.R;
import com.utouu.module.login.util.StatusBarUtil;
import com.utouu.module.login.util.TintStatusBar;

import org.simple.eventbus.EventBus;


/**
 * User: Administrator
 * Date: 2016-05-24 16:47
 */
public class BaseActivity extends FragmentActivity implements DialogInterface.OnCancelListener {

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TintStatusBar.translucentStatusBar(this);
        StatusBarUtil.StatusBarLightMode(this);
        try {
            EventBus.getDefault().register(this);
        } catch (NoClassDefFoundError ignored) {
        }
    }

    protected void setTitleBar(CharSequence title) {
//        TextView tvBack = (TextView)findViewById(R.id.tv_back);
        ImageView tvBack = (ImageView)findViewById(R.id.left_imageView);
        if (tvBack != null) {
            tvBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
        setTitleTextView(title);
    }

    protected void setTitleTextView(CharSequence title){
        TextView titleTextView = (TextView)findViewById(R.id.title_textView);
        if (titleTextView != null) {
            titleTextView.setText(title);
        }
    }

    protected void setRightImageView(int resId, View.OnClickListener onClickListener) {
        ImageView rightImageView = (ImageView)findViewById(R.id.right_imageView);
        if (rightImageView != null) {
            rightImageView.setVisibility(View.VISIBLE);
            rightImageView.setImageResource(resId);
            rightImageView.setOnClickListener(onClickListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UtouuHttpUtils.cancelRequests(this);
    }

    protected void showProgress() {
        try {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage(getString(R.string.commons_loading_hint));
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setOnCancelListener(this);
            }
            if (null != progressDialog && !progressDialog.isShowing()) {
                progressDialog.show();
            }
        } catch (Exception e) {
        }
    }

    protected void showProgress(String showContent) {
        try {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage(getString(R.string.commons_loading_hint));
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setOnCancelListener(this);
            }
            if (null != progressDialog) {
                if (StringUtils.isNotBlank(showContent)) {
                    progressDialog.setMessage(showContent);
                }
                if (!progressDialog.isShowing()) {
                    progressDialog.show();
                }
            }
        } catch (Exception e) {
        }
    }

    protected void dismissProgress() {
        try {
            if (null != progressDialog && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {
        UtouuHttpUtils.cancelRequests(this);
    }
}
