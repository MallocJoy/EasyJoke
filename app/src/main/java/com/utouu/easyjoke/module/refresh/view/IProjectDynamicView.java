package com.utouu.easyjoke.module.refresh.view;

import cn.utsoft.xunions.base.IBaseStatusView;
import cn.utsoft.xunions.entity.ProjectNewsEntity_HSC;
import cn.utsoft.xunions.entity.SpaceNewsEntity_HSC;

/**
 * Created by chenxin on 2017/2/19.
 * Function:
 * Desc:
 */

public interface IProjectDynamicView extends IBaseStatusView {

    /**
     * 获取项目动态详情
     * @param entity 实体类
     */
    void onGetProjectNewsSuccess(ProjectNewsEntity_HSC entity);

    /**
     * 获取空间新闻动态
     * @param entity 实体类
     */
    void onGetSpaceNewsSuccess(SpaceNewsEntity_HSC entity);

    void tgtInvalid(String message);

    void handleFailed(String message);
}
