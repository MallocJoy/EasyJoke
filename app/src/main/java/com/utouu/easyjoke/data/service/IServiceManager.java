package com.utouu.easyjoke.data.service;

import com.utouu.easyjoke.entity.login.User;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Create by 黄思程 on 2016/12/16   15:43
 * Function：网络访问接口定义
 * Desc：
 */
interface IServiceManager {

    @GET("stream/mix/v1/")
    Observable<ResponseBody>  getVideoJsonStr(
            @Query("content_type") int content_type);

    @GET("/login")
    Observable<String> login(
            @Query("username") String username,
            @Query("password") String password);

    @GET("/user")
    Observable<User>  getUser(
            @Query("token") String token);
}
