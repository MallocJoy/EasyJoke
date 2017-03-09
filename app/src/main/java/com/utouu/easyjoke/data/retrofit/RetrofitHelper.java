package com.utouu.easyjoke.data.retrofit;

import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import cn.utsoft.xunions.data.service.ICollectService;
import cn.utsoft.xunions.data.service.IOrderDetailService;
import cn.utsoft.xunions.data.service.IPersonalService;
import cn.utsoft.xunions.data.service.ITransferProjectsService;
import cn.utsoft.xunions.module.invest.IProjectService;
import cn.utsoft.xunions.util.constants.RequestHttpURL;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by marno on 2016/6/26/23:15.
 * retrofit封装
 */
public class RetrofitHelper {

    private static volatile Retrofit sRetrofit;
    private static volatile RetrofitHelper sHelper;

    private RetrofitHelper() {

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Response response = chain.proceed(request);
                        String s = request.url().encodedPath();
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
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(RequestHttpURL.BASE_URL)
                .build();
    }

    public static RetrofitHelper getIns() {
        if (sHelper == null) {
            synchronized (RetrofitHelper.class) {
                if (sHelper == null) {
                    sHelper = new RetrofitHelper();
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
    public IPersonalService USERINFOR() {
        return create(IPersonalService.class);
    }

    /**
     * Function:获取项目相关
     */
    public IProjectService PROJECT() {
        return create(IProjectService.class);
    }

    public ICollectService COLLECT() {
        return create(ICollectService.class);
    }


    public ITransferProjectsService getTransferProjects() {
        return create(ITransferProjectsService.class);
    }

    /**
     * 获取订单相关
     *
     * @return
     */
    public IOrderDetailService getOrderAbout() {
        return create(IOrderDetailService.class);
    }

}