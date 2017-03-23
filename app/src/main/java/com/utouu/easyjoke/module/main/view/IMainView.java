package com.utouu.easyjoke.module.main.view;

import com.utouu.easyjoke.base.IBaseStatusView;
import com.utouu.easyjoke.entity.BaseEntity;
import com.utouu.easyjoke.entity.main.MainEntity;

/**
 * Create by 黄思程 on 2017/3/20   17:25
 * Function：
 * Desc：主页面的View层
 */
public interface IMainView extends IBaseStatusView {

    /**
     * 获取主页面的数据
     * @param entity
     */
    void onGetMainPageSuccess(BaseEntity<MainEntity> entity);

    /**
     * 获取数据失败
     * @param message
     */
    void onFailed(String message);

    /**
     * 获取数据完成
     * @param message
     */
    void onComplete(String message);
}
