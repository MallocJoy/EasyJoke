package com.utouu.easyjoke.data.http;

import android.content.Context;
import android.text.Html;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;
import com.utouu.android.commons.entity.UploadParams;
import com.utouu.android.commons.http.BaseCallback;
import com.utouu.android.commons.http.BaseResponseListener;
import com.utouu.android.commons.http.CheckLoginCallback;
import com.utouu.android.commons.http.CheckLoginResponseListener;
import com.utouu.android.commons.http.UploadCallback;
import com.utouu.android.commons.http.UploadCheckLoginCallback;
import com.utouu.android.commons.presenter.protocol.BaseProtocol;
import com.utouu.android.commons.utils.GsonUtils;
import com.utouu.android.commons.utils.NetWorkUtils;

import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpStatus;

/**
 * Created by Du_Li on 2017/2/23 15:03.
 * Function:
 * Desc:
 */

/**
 * http请求类
 * Created by yang on 15/11/3.
 */
public class XunionsHttpUtils {

    public static boolean isDebug = true;

    public static final String NETWORK_FAILURE = "未连接到网络, 请稍候再试...";

    public static final String REQUEST_FAILURE = "数据请求出错, 请稍候再试...";

    public static final String RESPONSE_FAILURE = "数据返回出错, 请稍候再试...";

    public static final String RESPONSE_GSON_FAILURE = "数据解析出错, 请稍候再试...";

    private static Type jsonType = new TypeToken<BaseProtocol<JsonElement>>() {
    }.getType();

    private static boolean isSuccess(int statusCode) {
        return statusCode >= HttpStatus.SC_OK && statusCode < HttpStatus.SC_MULTIPLE_CHOICES;
    }

    /**
     * @param time timeout in milliseconds
     */
    public static void setUploadTimeOut(int time) {
        XunionsAsyncHttpUtils.setUploadTimeOut(time);
    }

    /**
     * @param time timeout in milliseconds
     */
    public static void setTimeOut(int time) {
        XunionsAsyncHttpUtils.setTimeOut(time);
    }

    /**
     * 设置请求头UserAgent参数
     *
     * @param userAgent 前缀 项目名称/项目版本号
     */
    public static void setUserAgent(final String userAgent) {
        XunionsAsyncHttpUtils.setUserAgent(userAgent);
    }

    /**
     * 设置最大重复请求次数
     *
     * @param retryCount 次数
     */
    public static void setRetryCount(final int retryCount) {
        XunionsAsyncHttpUtils.setRetryCount(retryCount);
    }

    /**
     * 返回接口请求所有数据(检查登录)
     *
     * @param context
     * @param requestUrl         数据地址
     * @param params             参数集合
     * @param checkLoginCallback 数据请求回调
     */
    public static void loadAllData(Context context, final String requestUrl, HashMap<String, String> params, final CheckLoginCallback checkLoginCallback) {
        loadData(context, requestUrl, params, checkLoginCallback, true);
    }

    /**
     * 返回接口请求所有数据(不检查登录)
     *
     * @param context
     * @param requestUrl   数据地址
     * @param params       参数集合
     * @param baseCallback 数据请求回调
     */
    public static void loadAllData(Context context, final String requestUrl, HashMap<String, String> params, final BaseCallback baseCallback) {
        loadData(context, requestUrl, params, baseCallback, true);
    }

    /**
     * 通用请求数据方法(检查登录)
     *
     * @param context
     * @param requestUrl         数据地址
     * @param params             参数集合
     * @param checkLoginCallback 数据请求回调
     */
    public static void loadData(Context context, final String requestUrl, HashMap<String, String> params, final CheckLoginCallback checkLoginCallback) {
        loadData(context, requestUrl, params, checkLoginCallback, false);
    }

    private static void loadData(Context context, final String requestUrl, HashMap<String, String> params, final CheckLoginCallback checkLoginCallback,
                                 final boolean resultAll) {
        if (checkLoginCallback == null) {
            throw new RuntimeException("add callback");
        }

        if (context == null) {
            checkLoginCallback.failure(REQUEST_FAILURE);
            return;
        }

        if (!NetWorkUtils.isConnected(context)) {
            checkLoginCallback.failure(NETWORK_FAILURE);
            return;
        }

        XunionsAsyncHttpUtils.post(context, requestUrl, params, new CheckLoginResponseListener() {
            @Override
            public void onTgtInvalid(String message) {
                if (isDebug) {
                    Log.i("commons", "onTgtInvalid: " + requestUrl + "\n content ->" + message);
                }
                checkLoginCallback.tgtInvalid(message);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                println(false, requestUrl, statusCode, responseString, throwable);
                checkLoginCallback.failure(REQUEST_FAILURE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                println(true, requestUrl, statusCode, responseString, null);
                if (isSuccess(statusCode)) {
                    if (resultAll) {
                        checkLoginCallback.success(responseString);
                    } else {
                        BaseProtocol<JsonElement> baseProtocol = null;
                        try {
                            baseProtocol = GsonUtils.getGson().fromJson(responseString, jsonType);
                        } catch (Exception e) {
                        }

                        if (baseProtocol != null) {
                            if (baseProtocol.success) {
                                checkLoginCallback.success(GsonUtils.getGson().toJson(baseProtocol.data));
                            } else {
                                checkLoginCallback.failure(baseProtocol.code, baseProtocol.msg);
                            }
                        } else {
                            checkLoginCallback.failure(RESPONSE_GSON_FAILURE);
                        }
                    }
                } else {
                    checkLoginCallback.failure(RESPONSE_FAILURE);
                }
            }
        });
    }

    /**
     * 通用请求数据方法(不检查登录)
     *
     * @param context
     * @param requestUrl   数据地址
     * @param params       参数集合
     * @param baseCallback 数据请求回调
     */
    public static void loadData(Context context, final String requestUrl, HashMap<String, String> params, final BaseCallback baseCallback) {
        loadData(context, requestUrl, params, baseCallback, false);
    }

    private static void loadData(Context context, final String requestUrl, HashMap<String, String> params, final BaseCallback baseCallback,
                                 final boolean resultAll) {
        if (baseCallback == null) {
            throw new RuntimeException("add callback");
        }

        if (context == null) {
            baseCallback.failure(REQUEST_FAILURE);
            return;
        }

        if (!NetWorkUtils.isConnected(context)) {
            baseCallback.failure(NETWORK_FAILURE);
            return;
        }

        XunionsAsyncHttpUtils.post(context, requestUrl, params, new BaseResponseListener() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                println(false, requestUrl, statusCode, responseString, throwable);
                baseCallback.failure(REQUEST_FAILURE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                println(true, requestUrl, statusCode, responseString, null);
                if (isSuccess(statusCode)) {
                    if (resultAll) {
                        baseCallback.success(responseString);
                    } else {
                        BaseProtocol<JsonElement> baseProtocol = null;
                        try {
                            baseProtocol = GsonUtils.getGson().fromJson(responseString, jsonType);
                        } catch (Exception e) {
                        }

                        if (baseProtocol != null) {
                            if (baseProtocol.success) {
                                baseCallback.success(GsonUtils.getGson().toJson(baseProtocol.data));
                            } else {
                                baseCallback.failure(baseProtocol.code, baseProtocol.msg);
                            }
                        } else {
                            baseCallback.failure(RESPONSE_GSON_FAILURE);
                        }
                    }
                } else {
                    baseCallback.failure(RESPONSE_FAILURE);
                }
            }
        });
    }

    /**
     * 通用请求数据方法(检查登录)
     *
     * @param context
     * @param requestUrl         数据地址
     * @param params             参数集合
     * @param checkLoginCallback 数据请求回调
     */
    public static void operation(final Context context, final String requestUrl, HashMap<String, String> params, final CheckLoginCallback checkLoginCallback) {
        if (checkLoginCallback == null) {
            throw new RuntimeException("add callback");
        }

        if (context == null) {
            checkLoginCallback.failure(REQUEST_FAILURE);
            return;
        }

        if (!NetWorkUtils.isConnected(context)) {
            checkLoginCallback.failure(NETWORK_FAILURE);
            return;
        }

        XunionsAsyncHttpUtils.post(context, requestUrl, params, new CheckLoginResponseListener() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                println(false, requestUrl, statusCode, responseString, throwable);
                checkLoginCallback.failure(REQUEST_FAILURE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                println(true, requestUrl, statusCode, responseString, null);
                if (isSuccess(statusCode)) {
                    BaseProtocol<JsonElement> baseProtocol = null;
                    try {
                        baseProtocol = GsonUtils.getGson().fromJson(responseString, jsonType);
                    } catch (Exception e) {
                    }

                    if (null != baseProtocol) {
                        if (baseProtocol.success) {
                            checkLoginCallback.success(baseProtocol.msg);
                        } else {
                            checkLoginCallback.failure(baseProtocol.code, baseProtocol.msg);
                        }
                    } else {
                        checkLoginCallback.failure(RESPONSE_GSON_FAILURE);
                    }
                } else {
                    checkLoginCallback.failure(RESPONSE_FAILURE);
                }
            }

            @Override
            public void onTgtInvalid(String message) {
                checkLoginCallback.tgtInvalid(message);
            }
        });
    }

    /**
     * 通用请求数据方法(不检查登录)
     *
     * @param context
     * @param requestUrl   数据地址
     * @param params       参数集合
     * @param baseCallback 数据请求回调
     */
    public static void operation(final Context context, final String requestUrl, HashMap<String, String> params, final BaseCallback baseCallback) {
        if (baseCallback == null) {
            throw new RuntimeException("add callback");
        }

        if (context == null) {
            baseCallback.failure(REQUEST_FAILURE);
            return;
        }

        if (!NetWorkUtils.isConnected(context)) {
            baseCallback.failure(NETWORK_FAILURE);
            return;
        }

        XunionsAsyncHttpUtils.post(context, requestUrl, params, new BaseResponseListener() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                println(false, requestUrl, statusCode, responseString, throwable);
                baseCallback.failure(REQUEST_FAILURE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                println(true, requestUrl, statusCode, responseString, null);
                if (isSuccess(statusCode)) {
                    BaseProtocol<JsonElement> baseProtocol = null;
                    try {
                        baseProtocol = GsonUtils.getGson().fromJson(responseString, jsonType);
                    } catch (Exception e) {
                    }

                    if (null != baseProtocol) {
                        if (baseProtocol.success) {
                            baseCallback.success(baseProtocol.msg);
                        } else {
                            baseCallback.failure(baseProtocol.code, baseProtocol.msg);
                        }
                    } else {
                        baseCallback.failure(RESPONSE_GSON_FAILURE);
                    }
                } else {
                    baseCallback.failure(RESPONSE_FAILURE);
                }
            }
        });
    }

    /**
     * 文件上传
     *
     * @param context
     * @param requestUrl
     * @param uploadParams
     * @param uploadCallback
     * @throws FileNotFoundException
     */
    public static void upload(Context context, final String requestUrl, UploadParams uploadParams, HashMap<String, String> params,
                              final UploadCallback uploadCallback) throws FileNotFoundException {
        if (uploadCallback == null) {
            throw new RuntimeException("add callback");
        }

        if (context == null) {
            uploadCallback.failure(REQUEST_FAILURE);
            return;
        }

        if (!NetWorkUtils.isConnected(context)) {
            uploadCallback.failure(NETWORK_FAILURE);
            return;
        }

        XunionsAsyncHttpUtils.uploadFile(context, requestUrl, uploadParams, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                println(false, requestUrl, statusCode, responseString, throwable);
                uploadCallback.failure(REQUEST_FAILURE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                println(true, requestUrl, statusCode, responseString, null);
                if (isSuccess(statusCode)) {
                    uploadCallback.success(responseString);
                } else {
                    uploadCallback.failure(RESPONSE_FAILURE);
                }
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                uploadCallback.onProgress(bytesWritten, totalSize);
            }
        });
    }

    public static void upload(Context context, final String requestUrl, UploadParams uploadParams, HashMap<String, String> params,
                              final UploadCheckLoginCallback uploadCheckLoginCallback) throws FileNotFoundException {
        if (uploadCheckLoginCallback == null) {
            throw new RuntimeException("add callback");
        }

        if (context == null) {
            uploadCheckLoginCallback.failure(REQUEST_FAILURE);
            return;
        }

        if (!NetWorkUtils.isConnected(context)) {
            uploadCheckLoginCallback.failure(NETWORK_FAILURE);
            return;
        }

        XunionsAsyncHttpUtils.uploadFile(context, requestUrl, uploadParams, params, new CheckLoginResponseListener() {
            @Override
            public void onTgtInvalid(String message) {
                uploadCheckLoginCallback.tgtInvalid(message);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                println(false, requestUrl, statusCode, responseString, throwable);
                uploadCheckLoginCallback.failure(REQUEST_FAILURE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                println(true, requestUrl, statusCode, responseString, null);
                uploadCheckLoginCallback.success(responseString);
            }
        });
    }

    private static void println(boolean isSuccess, String requestUrl, int statusCode, String responseString, Throwable throwable) {
        if (isDebug) {
            if (isSuccess) {
                Log.i("commons", "onSuccess: " + requestUrl + "\nstatusCode ->" + statusCode + " content ->" + responseString);
            } else {
                if (responseString != null) {
                    responseString = Html.fromHtml(responseString).toString();
                }
                String errorMsg = throwable != null ? throwable.getMessage() : "is null";
                Log.i("commons", "onFailure: " + requestUrl + "\nstatusCode ->" + statusCode + " content ->" + responseString + ", " +
                        "errorMsg ->" + errorMsg);
            }
        }
    }

    /**
     * 取消http请求
     *
     * @param context 上下文
     */

    public static void cancelRequests(Context context) {
        if (null != context) {
            XunionsAsyncHttpUtils.cancelRequests(context);
        }
    }
}
