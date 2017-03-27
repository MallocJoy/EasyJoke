package com.utouu.easyjoke.data.retrofit;

import com.orhanobut.logger.Logger;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Create by 黄思程 on 2017/3/20  14:08
 * Function：
 * Desc：Retrofit工具类
 */
public class RetrofitHelper {

    private static Retrofit sRetrofit;
    private static RetrofitHelper sHelper;

    private  RetrofitHelper() {

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    Response response = chain.proceed(request);
                    String s = request.url().encodedPath();
                    Logger.e(s);
                    return response;
                })
                .retryOnConnectionFailure(true)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        sRetrofit = new Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(RequestHttpURL.BASE_URL)
                .build();
    }

    private static RetrofitHelper getIns() {
        if (sHelper == null) {
            synchronized (RetrofitHelper.class) {
                if (sHelper == null) {
                    sHelper = new RetrofitHelper();
                }
            }
        }
        return sHelper;
    }

    public static <T> T create(Class<T> apiService) {
        return sHelper.getIns().sRetrofit.create(apiService);
    }
}