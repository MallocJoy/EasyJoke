package com.utouu.module.login.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.utouu.android.commons.utils.StringUtils;
import com.utouu.android.commons.utils.ToastUtils;
import com.utouu.module.login.R;

import org.json.JSONException;
import org.json.JSONObject;

public class WebActivity extends BaseActivity {

    private static final String ARG_PARAMS_TITLE = "title";
    private static final String ARG_PARAMS_URL = "url";

    private ProgressBar loadingProgressBar;
    private WebView contentWebView;

    private boolean isLoading;

    private boolean isCancel = true;
    private static PaymentListener paymentListener;

    public static final void start(Context context, String title, String requestUrl) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(ARG_PARAMS_TITLE, title);
        intent.putExtra(ARG_PARAMS_URL, requestUrl);
        context.startActivity(intent);
    }

    public static final void start(Context context, String title, String requestUrl,PaymentListener paymentListener1) {
        if (context == null) {
            return;
        }
        paymentListener = paymentListener1;
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(ARG_PARAMS_TITLE, title);
        intent.putExtra(ARG_PARAMS_URL, requestUrl);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        String tempTitle = "";
        String tempUrl = "";
        if (getIntent() != null) {
            tempTitle = getIntent().getStringExtra(ARG_PARAMS_TITLE);
            tempUrl = getIntent().getStringExtra(ARG_PARAMS_URL);
        }

        setTitleBar(tempTitle);
        setRightImageView(R.mipmap.icon_refresh, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isLoading) {
                    if (contentWebView != null) {
                        contentWebView.reload();
                    }
                }
            }
        });

        loadingProgressBar = (ProgressBar)findViewById(R.id.loading_progressBar);
        contentWebView = (WebView)findViewById(R.id.content_webView);
        if (contentWebView != null) {
            WebSettings webSettings = contentWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
            webSettings.setSupportZoom(true);
            webSettings.setBuiltInZoomControls(true);
            webSettings.setDisplayZoomControls(false);
            webSettings.setUseWideViewPort(true);
            webSettings.setLoadWithOverviewMode(true);
            contentWebView.setWebViewClient(new CustomWebViewClient());
            contentWebView.setWebChromeClient(new CustomWebChromeClient());

            if (StringUtils.isNotBlank(tempUrl)) {
                contentWebView.loadUrl(tempUrl);
            } else {
                ToastUtils.showShortToast(this, "request URL is null.");
                finish();
            }
        } else {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (contentWebView != null) {
            contentWebView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (contentWebView != null) {
            contentWebView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (contentWebView != null) {
            contentWebView.stopLoading();
            contentWebView.destroy();
            contentWebView = null;
        }

        if(paymentListener != null && this.isCancel) {
            paymentListener.onCancel();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == MotionEvent.ACTION_DOWN) {
            if (contentWebView != null && contentWebView.canGoBack()) {
                contentWebView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private class CustomWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
            super.onPageStarted(webView, s, bitmap);
            isLoading = true;
        }

        @Override
        public void onPageFinished(WebView webView, String s) {
            super.onPageFinished(webView, s);
            isLoading = false;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(android.text.TextUtils.isEmpty(url)) {
                return super.shouldOverrideUrlLoading(view, url);
            } else {
                if(url.startsWith("data:")) {
                    String jsonString = url.replace("data:", "");

                    try {
                        JSONObject e = new JSONObject(jsonString);
                        boolean success = e.getBoolean("success");
                        if(success) {
                            WebActivity.this.success();
                        } else {
                            boolean hasMsg = e.has("msg");
                            String errorMsg = WebActivity.this.getString(R.string.pay_failure);
                            if(hasMsg) {
                                errorMsg = e.getString("msg");
                            }

                            WebActivity.this.failure(errorMsg);
                        }
                    } catch (JSONException var8) {
                        WebActivity.this.failure(WebActivity.this.getString(R.string.pay_failure));
                    }
                } else {
                    view.loadUrl(url);
                }

                return true;
            }
        }
    }

    private class CustomWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView webView, int i) {
            if (null != loadingProgressBar) {
                loadingProgressBar.setProgress(i);
                loadingProgressBar.setVisibility(i == 100 ? View.GONE : View.VISIBLE);
            }
        }

        @Override
        public boolean onJsAlert(WebView webView, String url, String message, final JsResult jsResult) {
            new AlertDialog.Builder(webView.getContext(), R.style.commons_baseDialog2)
                    .setTitle(getString(R.string.commons_prompt1)).setMessage(message).setCancelable(false)
                    .setPositiveButton(android.R.string.ok, null).create().show();
            if (jsResult != null) {
                jsResult.confirm();
            }
            return true;
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (StringUtils.isNotBlank(title)) {
                setTitleTextView(title);
            }
        }
    }

    private void success() {
        this.isCancel = false;
        if(paymentListener != null) {
            paymentListener.onSuccess();
        }

        this.finish();
    }

    private void failure(String msg) {
        this.isCancel = false;
        if(paymentListener != null) {
            paymentListener.onFailure(msg);
        }

        this.finish();
    }
}
