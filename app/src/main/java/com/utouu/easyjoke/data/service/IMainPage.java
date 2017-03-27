package com.utouu.easyjoke.data.service;

import com.utouu.easyjoke.data.retrofit.RequestHttpURL;
import com.utouu.easyjoke.entity.BaseEntity;
import com.utouu.easyjoke.entity.main.MainEntity;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Create by 黄思程 on 2017/3/21   9:24
 * Function：
 * Desc：
 */
public interface IMainPage {

    @GET(RequestHttpURL.GET_MAIN_PAGE_INFO) //获取主页面数据
    Observable<BaseEntity<List<MainEntity>>> GetMainPage();

}
