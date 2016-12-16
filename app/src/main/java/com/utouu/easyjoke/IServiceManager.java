package com.utouu.easyjoke;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Create by 黄思程 on 2016/12/16   15:43
 * Function：网络访问接口定义
 * Desc：
 */
public interface IServiceManager {

    @GET("stream/mix/v1/")
    Observable<ResponseBody>  getVideoJsonStr(@Query("content_type") int content_type);
}
