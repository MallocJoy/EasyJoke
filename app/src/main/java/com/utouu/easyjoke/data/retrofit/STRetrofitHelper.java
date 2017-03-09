package com.utouu.easyjoke.data.retrofit;

import com.orhanobut.logger.Logger;
import com.utouu.android.commons.constants.HttpURLConstant;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import cn.utsoft.xunions.data.service.IPersonalService;
import cn.utsoft.xunions.module.invest.IProjectService;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by marno on 2016/6/26/23:15.
 * retrofit封装
 */
public class STRetrofitHelper {

    //    private static String URL_BASE = BuildConfig.BASE_URL;
    private static volatile Retrofit sRetrofit;
    private static volatile STRetrofitHelper sHelper;

    private STRetrofitHelper() {

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Response response = chain.proceed(request);
                        String s = request.url().encodedPath();
                        String s1 = request.url().host();
                        Logger.e(s);
                        return response;
                    }
                })
                .retryOnConnectionFailure(true)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
        sRetrofit = new Retrofit.Builder()
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(HttpURLConstant.baseHttpURL.getTickets() + File.separator)
                .build();
    }

    public static STRetrofitHelper getIns() {
        if (sHelper == null) {
            synchronized (STRetrofitHelper.class) {
                if (sHelper == null) {
                    sHelper = new STRetrofitHelper();
                }
            }
        }
        return sHelper;
    }

    protected static <T> T create(Class<T> apiService) {
        return sHelper.getIns().sRetrofit.create(apiService);
    }


    /**
     * Function:获取用户信息
     */
    public IPersonalService ST() {
        return create(IPersonalService.class);
    }

//    public IInvestStarService getInvestStar() {
//        return create(IInvestStarService.class);
//    }

    /**
     * Function:获取项目相关
     */
    public IProjectService PROJECT() {
        return create(IProjectService.class);
    }
}