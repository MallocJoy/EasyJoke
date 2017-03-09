package com.utouu.easyjoke.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Environment;

import com.utouu.android.commons.http.CheckLoginCallback;
import com.utouu.android.commons.utils.GsonUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.util.HashMap;

import cn.utsoft.xunions.base.BaseActivity;
import cn.utsoft.xunions.data.http.XunionsHttpUtils;
import cn.utsoft.xunions.entity.CheckVersionEntity_HT;
import cn.utsoft.xunions.util.constants.RequestHttpURL;
import okhttp3.Call;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by cj on 2017/3/8.
 * Function:
 * Desc:检查版本升级的工具类
 */

public class CheckVersionUtil {
    private static CheckVersionUtil checkVersionUtil;
    private Activity mActivity;
    private boolean isUpdate = false;

    private CheckVersionUtil() {
    }

    public static CheckVersionUtil getInstance() {
        if (null == checkVersionUtil) {
            synchronized (CheckVersionUtil.class) {
                if (null == checkVersionUtil) {
                    checkVersionUtil = new CheckVersionUtil();
                }
            }
        }
        return checkVersionUtil;
    }


    public void checkVersion(Activity activity) {
        if (activity == null) return;
        this.mActivity = activity;
        HashMap<String, String> map = new HashMap<>();
        map.put("terrace", "0");
        map.put("version", "1.9.10");
        comparisonVersion(map);
    }

    /**
     * 和服务器版本进行比对
     *
     * @param map
     */
    private void comparisonVersion(HashMap<String, String> map) {
        XunionsHttpUtils.loadData(mActivity, RequestHttpURL.CHECK_APP_VERSION, map, new CheckLoginCallback() {
            @Override
            public void tgtInvalid(String message) {
                try {
                    ((BaseActivity) mActivity).tgtInvalid(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    LoggerUtil.e(e.toString());
                }
            }

            @Override
            public void success(String message) {
                LoggerUtil.d(message);
                CheckVersionEntity_HT entity = null;
                try {
                    LoggerUtil.json(message);
                    entity = GsonUtils.getGson().fromJson(message, CheckVersionEntity_HT.class);
                } catch (Exception e) {
                    LoggerUtil.e("checkVersion:" + e.getMessage());
                }
                checkVersion(entity);
            }

            @Override
            public void failure(String message) {
                LoggerUtil.e(message);
            }

            @Override
            public void failure(String statusCode, String message) {
                LoggerUtil.e(statusCode + "检查更新》》》" + message);
            }
        });
    }

    /**
     * 新开线程校验版本是否需要下载更新
     *
     * @param entity
     */
    private void checkVersion(final CheckVersionEntity_HT entity) {
        isUpdate = false;
        Observable.just(true)
                .filter(new Func1<Boolean, Boolean>() {
                    @Override
                    public Boolean call(Boolean aBoolean) {
                        LoggerUtil.d("aBoolean:" + aBoolean);
                        if (entity == null || entity.versionNum == null || entity.versionSite == null || entity.versionSite.isEmpty() || entity.versionNum.isEmpty()) {
                            isUpdate = false;
                            return false;
                        }
                        return isUpdate;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        LoggerUtil.d("isUpdate:" + isUpdate);
                        //showAlert(isUpdate, entity);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {

                    }
                });
    }

    private void showAlert(boolean isUpdate, CheckVersionEntity_HT entity) {
        AlertUtil.show(mActivity, "更新提示", "由于系统升级,您的版本已停止服务,\n请及时更新到最新版本!", "", "ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    //下载新版本
                    loadApk("http://119.6.238.79:9999/113.207.4.91/appdl.hicloud.com/dl/appdl/application/apk/b4/b450b379032e40df8bc5cba5d088c9ee/com.ss.android.article.news.1703011436.apk");
                }
            }
        });
//        AlertUtil.show(mActivity, "有新版本啦", "为了更好的为您服务,\n建议您更新到最新版本!", "忽略此版本", "立即更新", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if (which == DialogInterface.BUTTON_POSITIVE) {
//                    //下载新版本
//                }
//            }
//        });
    }

    private void loadApk(String url) {
        if (url == null || url.isEmpty()) {
            return;
        }
        final ProgressDialog mProgressDialog = new ProgressDialog(mActivity);
        mProgressDialog.setMessage("新版本下载中");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setMax(100);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.show();
        OkHttpUtils//
                .get()//
                .url(url)//
                .build()//
                .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(), "XUnions.apk")//
                {
                    @Override
                    public void inProgress(float progress, long total, int id) {
                        super.inProgress(progress, total, id);
                        mProgressDialog.setProgress((int) (100 * progress));
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LoggerUtil.d("onError:" + e.getMessage());
                        mProgressDialog.dismiss();
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        LoggerUtil.d("response:" + response.getAbsolutePath());
                        mProgressDialog.dismiss();
                        //下载完成调用安装
                        com.marno.easyutilcode.AppUtil.installApp(mActivity, response);
                    }
                });

    }
}
