package com.utouu.easyjoke.data.repository;

import android.accounts.NetworkErrorException;
import android.util.Log;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import cn.utsoft.xunions.data.exception.DefaultErrorException;
import cn.utsoft.xunions.entity.BaseEntity;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created: 李刚 on 2016/8/23/16:39.
 * Function: 封装接口调用及相应返回数据处理逻辑基类
 * Desc: AriesHoo 2016-11-14 15:01:22修改-增加统一返回错误处理
 */
public abstract class BaseRepository {

    public static String st = "DataConstant.DEFAULT_ST";

    protected <T> Observable<T> transform(final Observable<? extends BaseEntity<T>> observable) {
        return observable.subscribeOn(Schedulers.io())
                .doOnNext(new Action1<BaseEntity<T>>() {
                    @Override
                    public void call(BaseEntity<T> tBaseEntity) {
                        Log.d("retrofit", "doOnNext:" + Thread.currentThread().getName());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<BaseEntity<T>, Observable<T>>() {
                    @Override
                    public Observable<T> call(BaseEntity<T> result) {
                        if (result != null) {
                            Log.e("retrofit", new Gson().toJson(result));
                        }
                        if (result == null) {
                            return Observable.error(new NetworkErrorException());
                        } else if (result.success && result.code.equals("200")) {
                            Logger.e(result.toString());
                            return Observable.just(result.data);
                        } else if (!result.success && (result.code.equals("025") || result.code.equals("023"))) {
                            return Observable.error(new DefaultErrorException(result.code, result.msg));
                        } else {
                            return Observable.error(new Exception());
                        }
                    }
                });
    }
}
