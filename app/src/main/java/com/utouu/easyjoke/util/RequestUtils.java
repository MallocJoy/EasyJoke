package com.utouu.easyjoke.util;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.utouu.android.commons.constants.DataConstant;
import com.utouu.android.commons.utils.GenerateSignDemo;

import java.util.HashMap;
import java.util.Map;

import cn.utsoft.xunions.XUnionsApplication;
import cn.utsoft.xunions.data.repository.BaseRepository;
import cn.utsoft.xunions.data.retrofit.DefaultSubscriber;
import cn.utsoft.xunions.data.retrofit.STRetrofitHelper;
import cn.utsoft.xunions.data.service.IRequestListener;
import cn.utsoft.xunions.widgets.TgtInvalidDialog;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by chenxin on 2017/1/22 10:17.
 * Function: 获取请求头工具类
 * Desc:
 */

public class RequestUtils {

    /**
     * 获得sign签名
     */
    public static Map<String, String> getHeaders(Map<String, String> map, String st, String service) {

        final long time = System.currentTimeMillis();
        String generateCommonSign = GenerateSignDemo.generateCommonSign(time, map);
        Map<String, String> headers = new HashMap<>();
        headers.put("cas-client-sign", generateCommonSign);
        headers.put("cas-client-time", String.valueOf(time));
        headers.put("cas-client-service", service);
        headers.put("cas-client-st", BaseRepository.st);
        Logger.e(st);
        return headers;
    }

    public static Observable<String> getResultSt(String service) {
//        RequestHttpURL.USER_DETAIL_URL
        final String[] resultST = new String[1];
        Map<String, String> params = new HashMap<>();

        params.put("service", service);
        String localTGT = DataConstant.getLocalTGT(XUnionsApplication.getAppContext());
        if (TextUtils.isEmpty(localTGT)) {
            new TgtInvalidDialog(XUnionsApplication.getAppContext()).show();
            return null;
        }
        Observable<String> stringObservable = STRetrofitHelper.getIns().ST().getST(localTGT, params)
                .subscribeOn(Schedulers.io());

        return stringObservable;
    }


    /**
     * Function:网络请求工具类
     *
     * @param
     * @return
     */
    public static <K> void loadData(String service, final IRequestListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("service", service);
        String localTGT = DataConstant.getLocalTGT(XUnionsApplication.getAppContext());

        if (TextUtils.isEmpty(localTGT)) {
            if (listener != null) {
                listener.tgtInvalid("TGT失效,请重新获取");
            }
            return;
        }
        STRetrofitHelper.getIns().ST().getST(localTGT, params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscriber<String>() {
                    @Override
                    public void _onNext(String entity) {
                        LoggerUtil.e(entity);

                        if (listener != null) {
                            listener.success(entity);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (listener != null) {
                            listener.tgtInvalid(e.getMessage());
                        }
                    }
                });
    }
}
