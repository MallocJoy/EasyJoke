package com.utouu.easyjoke.module.refresh.presenter;

import android.content.Context;

import com.google.gson.JsonSyntaxException;
import com.utouu.android.commons.http.CheckLoginCallback;
import com.utouu.android.commons.utils.GsonUtils;
import com.utouu.easyjoke.module.refresh.view.IProjectDynamicView;

import java.util.HashMap;

import cn.utsoft.xunions.base.BasePresenter;
import cn.utsoft.xunions.data.http.XunionsHttpUtils;
import cn.utsoft.xunions.entity.ProjectNewsEntity_HSC;
import cn.utsoft.xunions.entity.SpaceNewsEntity_HSC;
import cn.utsoft.xunions.util.constants.RequestHttpURL;

/**
 * Created by chenxin on 2017/2/19.
 * Function:
 * Desc:
 */

public class ProjectDynamicPresenter extends BasePresenter<IProjectDynamicView> {

    /**
     * 获取项目动态
     * @param context
     * @param newId
     */
    public void requestProjectNews(Context context,String newId){
        getView().loading();
        HashMap<String, String> params = new HashMap<>();
        params.put("newId",newId);
        XunionsHttpUtils.loadData(context, RequestHttpURL.GET_PROJECT_NEWS, params, new CheckLoginCallback() {
            @Override
            public void tgtInvalid(String message) {
                getView().content();
                getView().tgtInvalid(message);
            }

            @Override
            public void success(String message) {
                getView().content();

                try {
                    ProjectNewsEntity_HSC entity = GsonUtils.getGson().fromJson(message, ProjectNewsEntity_HSC.class);
                    if (entity != null)
                        getView().onGetProjectNewsSuccess(entity);
                    else
                        getView().empty();
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void failure(String message) {
                getView().handleFailed(message);
                getView().error(message);
            }

            @Override
            public void failure(String statusCode, String message) {
                getView().handleFailed(message);
                getView().error(message);
            }
        });
    }

    /**
     * 获取空间新闻
     * @param context
     * @param id
     */
    public void requestSpaceNews(Context context, String id){
        getView().loading();
        HashMap<String, String> params = new HashMap<>();
        params.put("id",id);

        XunionsHttpUtils.loadData(context, RequestHttpURL.GET_SPACE_NEWS, params, new CheckLoginCallback() {
            @Override
            public void tgtInvalid(String message) {
                getView().content();
                getView().tgtInvalid(message);
            }

            @Override
            public void success(String message) {
                getView().content();

                try {
                    SpaceNewsEntity_HSC entity = GsonUtils.getGson().fromJson(message, SpaceNewsEntity_HSC.class);
                    if (entity != null)
                        getView().onGetSpaceNewsSuccess(entity);
                    else
                        getView().empty();
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(String message) {
                getView().handleFailed(message);
                getView().error(message);
            }

            @Override
            public void failure(String statusCode, String message) {
                getView().handleFailed(message);
                getView().error(message);
            }
        });
    }

}
