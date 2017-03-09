package com.utouu.easyjoke.data.http;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.loopj.android.http.TextHttpResponseHandler;
import com.orhanobut.logger.Logger;
import com.utouu.android.commons.constants.DataConstant;
import com.utouu.android.commons.constants.HttpURLConstant;
import com.utouu.android.commons.entity.UploadParams;
import com.utouu.android.commons.http.CheckLoginResponseListener;
import com.utouu.android.commons.utils.GenerateSignDemo;
import com.utouu.android.commons.utils.StringUtils;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.HttpURLConnection;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.message.BasicHeader;

/**
 * Created by Du_Li on 2017/2/23 15:04.
 * Function:
 * Desc:
 */

/**
 * Created by yang on 15/11/3.
 */
public class XunionsAsyncHttpUtils {

    private static String resultST = DataConstant.DEFAULT_ST;

    private static final String MESSAGE_TGT_INVALID = "您的身份令牌已过期, 为了您的帐号安全, 请重新登录.";

    private static int defaultRetryCount = 4;

    private static final String HTTP_ACCEPT = "text/html;charset=UTF-8,application/json";
    private static final String HTTP_CONTENT_TYPE = "application/x-www-form-urlencoded;charset=UTF-8";
    private static final int HTTP_REQUEST_TIMEOUT = 20000;
    private static final int HTTP_RESPONSE_TIMEOUT = HTTP_REQUEST_TIMEOUT;
    private static final int HTTP_MAX_CONNECTIONS = 30;
    private static final String HTTP_CONNECTION = "close";

    private static final int UPLOAD_TIMEOUT = 60 * 1000;

    private static AsyncHttpClient asyncHttpClient = new AsyncHttpClient(true, 80, 443);
    private static AsyncHttpClient uploadAsyncHttpClient = new AsyncHttpClient(true, 80, 443);

    static {
        asyncHttpClient.setTimeout(HTTP_REQUEST_TIMEOUT);
        asyncHttpClient.setResponseTimeout(HTTP_RESPONSE_TIMEOUT);
        asyncHttpClient.setConnectTimeout(HTTP_REQUEST_TIMEOUT);

        asyncHttpClient.setMaxConnections(HTTP_MAX_CONNECTIONS);
        asyncHttpClient.addHeader("Accept", HTTP_ACCEPT);
        asyncHttpClient.addHeader("Content-Type", HTTP_CONTENT_TYPE);
        asyncHttpClient.addHeader("Connection", HTTP_CONNECTION);
        asyncHttpClient.setUserAgent(System.getProperty("http.agent", Build.FINGERPRINT));

        uploadAsyncHttpClient.setTimeout(UPLOAD_TIMEOUT);
        uploadAsyncHttpClient.setConnectTimeout(UPLOAD_TIMEOUT);
        uploadAsyncHttpClient.setResponseTimeout(UPLOAD_TIMEOUT);
    }

    /**
     * @param time timeout in milliseconds
     */
    public static void setUploadTimeOut(int time) {
        uploadAsyncHttpClient.setTimeout(time);
        uploadAsyncHttpClient.setConnectTimeout(time);
        uploadAsyncHttpClient.setResponseTimeout(time);
    }

    /**
     * @param time timeout in milliseconds
     */
    public static void setTimeOut(int time) {
        asyncHttpClient.setTimeout(time);
        asyncHttpClient.setResponseTimeout(time);
        asyncHttpClient.setConnectTimeout(time);
    }

    /**
     * 设置请求头UserAgent参数
     *
     * @param userAgent 前缀 项目名称/项目版本号
     */
    public static void setUserAgent(final String userAgent) {
        asyncHttpClient.setUserAgent(userAgent);
    }

    /**
     * 设置最大重复请求次数
     *
     * @param retryCount 次数
     */
    public static void setRetryCount(final int retryCount) {
        defaultRetryCount = retryCount;
        if (defaultRetryCount > 5) {
            defaultRetryCount = 4;
        }
    }


    /**
     * get请求数据方式
     *
     * @param context          上下文
     * @param url              请求数据地址
     * @param responseListener 请求成功后回调
     */
    public static void get(Context context, String url, ResponseHandlerInterface responseListener) {
        get(context, url, null, responseListener);
    }

    /**
     * get请求数据方式
     *
     * @param context          上下文
     * @param url              请求数据地址
     * @param params           请求参数
     * @param responseListener 请求成功后回调
     */
    public static void get(Context context, String url, HashMap<String, String> params, final ResponseHandlerInterface responseListener) {
        asyncHttpClient.get(context, url, (null != params ? new RequestParams(params) : null), responseListener);
    }

    /**
     * post请求方式
     *
     * @param context          上下文
     * @param url              请求数据地址
     * @param params           请求参数
     * @param responseListener 请求成功后回调
     */
    public static void post(Context context, String url, HashMap<String, String> params, final ResponseHandlerInterface responseListener) {
        asyncHttpClient.post(context, url, (null != params ? new RequestParams(params) : null), responseListener);
    }

    /**
     * @param context          上下文
     * @param requestUrl       请求数据地址
     * @param params           请求参数
     * @param responseListener 请求成功后回调
     */
    public static void post(final Context context, final String requestUrl, final HashMap<String, String> params,
                            final CheckLoginResponseListener responseListener) {
        post(context, requestUrl, params, 1, responseListener);
    }

    /**
     * @param context                    上下文
     * @param requestUrl                 请求数据地址
     * @param params                     请求参数
     * @param retryCount                 请求次数
     * @param checkLoginResponseListener 请求成功后回调
     */
    public static void post(final Context context, final String requestUrl, final HashMap<String, String> params, final int retryCount,
                            final CheckLoginResponseListener checkLoginResponseListener) {

        final Header[] requestHeaders = getPostHeaders(requestUrl, (null != params) ? params : new HashMap<String, String>());
        final RequestParams requestParams = (null != params && params.size() > 0) ? new RequestParams(params) : null;

        asyncHttpClient.post(context, requestUrl, requestHeaders, requestParams, null, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Logger.e("failure" + responseString);
                if (retryCount < defaultRetryCount) {
                    final int requestCount = retryCount + 1;
                    post(context, requestUrl, params, requestCount, checkLoginResponseListener);
                } else {
                    checkLoginResponseListener.onFailure(statusCode, headers, responseString, throwable);
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Logger.e("commons" + responseString);
                if (statusCode == HttpsURLConnection.HTTP_OK) {
                    boolean isSuccess = false;
                    String code = "";
                    try {
                        JSONObject resultJson = new JSONObject(responseString);
                        if (resultJson.has("success")) {
                            isSuccess = resultJson.getBoolean("success");
                        }
                        if (resultJson.has("code")) {
                            code = resultJson.getString("code");
                        }
                    } catch (Exception ignored) {
                    }

                    boolean retryST = false;
                    if (null != context && !isSuccess && (StringUtils.equals("023", code)) || (StringUtils.equals("025", code))) {
                        retryST = true;
                    }

                    if (retryST) {
                        String localTgt = DataConstant.getLocalTGT(context);
                        if (TextUtils.isEmpty(localTgt)) {//本地未保存st，需要重新登录
                            checkLoginResponseListener.onTgtInvalid(MESSAGE_TGT_INVALID);
                        } else { //st令牌失效， 需要重新获取新的st令牌
                            RequestParams stRequestParams = new RequestParams();
                            stRequestParams.put("service", requestUrl);
                            asyncHttpClient.post(context, HttpURLConstant.baseHttpURL.getTickets() + File.separator + localTgt, stRequestParams, new
                                    TextHttpResponseHandler() {
                                        @Override
                                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                            Logger.e("tgt>>>>>>>>>>" + responseString + "status>>>>>>" + statusCode + "/n" + HttpURLConstant.baseHttpURL.getTickets() + File.separator);
                                            if (statusCode >= 400 && statusCode < 500) {// tgt令牌失效
                                                checkLoginResponseListener.onTgtInvalid(MESSAGE_TGT_INVALID);
                                            } else {
                                                checkLoginResponseListener.onFailure(statusCode, headers, responseString, throwable);
                                            }
                                        }

                                        @Override
                                        public void onSuccess(int statusCode, Header[] resultStHeaders, String responseString) {
                                            Logger.e("st>>>>>>>" + responseString);
                                            if (statusCode == HttpsURLConnection.HTTP_OK) {
                                                resultST = responseString;
                                                post(context, requestUrl, params, checkLoginResponseListener);
                                            } else {
                                                checkLoginResponseListener.onTgtInvalid(MESSAGE_TGT_INVALID);
                                            }
                                        }
                                    });
                        }
                    } else {
                        checkLoginResponseListener.onSuccess(statusCode, headers, responseString);
                    }
                } else {
                    checkLoginResponseListener.onSuccess(statusCode, headers, responseString);
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
     * @param responseHandlerInterface
     * @throws FileNotFoundException
     */
    public static void uploadFile(Context context, String requestUrl, UploadParams uploadParams, HashMap<String, String> params,
                                  ResponseHandlerInterface responseHandlerInterface) throws FileNotFoundException {

        RequestParams requestParams = new RequestParams(params);
        if (uploadParams != null) {
            String fileName = uploadParams.fileName;
            String contentType = uploadParams.contentType;
            if (StringUtils.isNotBlank(contentType) && StringUtils.isNotBlank(fileName)) {
                requestParams.put(uploadParams.key, uploadParams.targetFile, contentType, fileName);
            } else {
                requestParams.put(uploadParams.key, uploadParams.targetFile);
            }
        }
        uploadAsyncHttpClient.post(context, requestUrl, requestParams, responseHandlerInterface);
    }

    public static void uploadFile(final Context context, final String requestUrl, UploadParams uploadParams, final HashMap<String, String> params,
                                  final CheckLoginResponseListener checkLoginResponseListener) throws FileNotFoundException {

        RequestParams requestParams = new RequestParams(params);
        if (uploadParams != null) {
            String fileName = uploadParams.fileName;
            String contentType = uploadParams.contentType;

            if (StringUtils.isNotBlank(contentType) && StringUtils.isNotBlank(fileName)) {
                requestParams.put(uploadParams.key, uploadParams.targetFile, contentType, fileName);
            } else {
                requestParams.put(uploadParams.key, uploadParams.targetFile);
            }
        }

        Header[] headers = new Header[3];
        headers[0] = new BasicHeader("cas-client-st", resultST);
        headers[1] = new BasicHeader("cas-client-time", String.valueOf(System.currentTimeMillis()));
        headers[2] = new BasicHeader("cas-client-sign", "cas-client-sign");


        uploadAsyncHttpClient.post(context, requestUrl, headers, requestParams, null, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                checkLoginResponseListener.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                boolean retryST = false;
                try {
                    if (statusCode == HttpURLConnection.HTTP_OK) {
                        JSONObject json = new JSONObject(responseString);
                        if (json.has("code")) {
//                            JSONObject dataJson = json.getJSONObject("msg");
//                            if (dataJson.has("code") && StringUtils.equals("025", dataJson.getString("code"))) {
                            if (json.getString("code").equals("025"))
                                retryST = true;
//                            }
                        }
                    }
                } catch (Exception ignored) {
                }

                if (retryST) {
                    String localTgt = DataConstant.getLocalTGT(context);
                    if (TextUtils.isEmpty(localTgt)) {//本地未保存st，需要重新登录
                        checkLoginResponseListener.onTgtInvalid(MESSAGE_TGT_INVALID);
                    } else { //st令牌失效， 需要重新获取新的st令牌
                        RequestParams stRequestParams = new RequestParams();
                        stRequestParams.put("service", requestUrl);
                        String getStUrl = HttpURLConstant.baseHttpURL.getTickets() + File.separator + localTgt;
                        asyncHttpClient.post(context, getStUrl, stRequestParams, new TextHttpResponseHandler() {
                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                if (null != context) {
                                    if (statusCode >= 400 && statusCode < 500) {// tgt令牌失效
                                        checkLoginResponseListener.onTgtInvalid(MESSAGE_TGT_INVALID);
                                    } else {
                                        checkLoginResponseListener.onFailure(statusCode, headers, responseString, throwable);
                                    }
                                }
                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] resultStHeaders, String responseString) {
                                if (null != context) {
                                    if (statusCode == HttpsURLConnection.HTTP_OK) {
                                        resultST = responseString;
                                        post(context, requestUrl, params, checkLoginResponseListener);
                                    } else {
                                        checkLoginResponseListener.onTgtInvalid(MESSAGE_TGT_INVALID);
                                    }
                                }
                            }
                        });
                    }
                } else {
                    checkLoginResponseListener.onSuccess(statusCode, headers, responseString);
                }
            }
        });
    }

    /**
     * 取消http请求
     *
     * @param context 上下文
     */

    public static void cancelRequests(Context context) {
        if (null != context) {
            asyncHttpClient.cancelRequests(context, true);
        }
    }

    public static void clearST() {
        resultST = DataConstant.DEFAULT_ST;
    }

    /**
     * 请求头参数
     *
     * @param service 接口请求地址
     * @param hashMap 接口请求参数
     * @return <br>
     * 创建时间：2015-7-28 下午4:14:51
     */
    public static Header[] getPostHeaders(String service, final HashMap<String, String> hashMap) {
        final long time = System.currentTimeMillis();
        String generateCommonSign = GenerateSignDemo.generateCommonSign(time, hashMap);
        Header[] headers = new Header[4];
        headers[0] = new BasicHeader("cas-client-sign", generateCommonSign);
        headers[1] = new BasicHeader("cas-client-time", String.valueOf(time));
        headers[2] = new BasicHeader("cas-client-service", service);
        headers[3] = new BasicHeader("cas-client-st", resultST);
        for (Header header : headers) {
            System.out.println(header.getName() + "::" + header.getValue());
        }
        return headers;
    }
}
