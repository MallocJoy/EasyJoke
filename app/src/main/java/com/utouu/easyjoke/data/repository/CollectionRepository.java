package com.utouu.easyjoke.data.repository;

import java.util.HashMap;
import java.util.Map;

import cn.utsoft.xunions.data.retrofit.RetrofitHelper;
import cn.utsoft.xunions.entity.CollectionEntity;
import cn.utsoft.xunions.entity.page.PageController;
import cn.utsoft.xunions.util.RequestUtils;
import cn.utsoft.xunions.util.TestRequestUtil;
import retrofit2.http.FieldMap;
import retrofit2.http.HeaderMap;
import rx.Observable;

/**
 * Created by 李波 on 2017/2/20.
 * Function:
 * Desc:
 */
public class CollectionRepository extends BaseRepository {

    private volatile static CollectionRepository instance;

    private CollectionRepository() {

    }

    public static CollectionRepository getIns() {
        if (instance == null) {
            synchronized (CollectionRepository.class) {
                instance = new CollectionRepository();
            }
        }
        return instance;
    }

    /**
     * 加载我的收藏信息
     *
     * @param page
     * @param size
     * @return
     */
    public Observable<PageController<CollectionEntity>> loadMyCollectionList(int page, int size) {
        Map<String, String> params = new HashMap<>();
        params.put("page", page + "");
        params.put("size", size + "");
        return transform(RetrofitHelper.getIns().COLLECT()
                .loadMyCollectionList(params, RequestUtils.getHeaders(params, TestRequestUtil.ST, null)));
    }

    /**
     * 删除我的收藏
     *
     * @param params
     * @param headers
     * @return
     */
    public Observable<String> deleteMyCollection(@FieldMap Map<String, String> params, @HeaderMap Map<String, String> headers) {
        return null;
    }

    /**
     * 添加我的收藏
     *
     * @param projectId // 项目id
     * @return
     */
    public Observable<CollectionEntity> createMyCollection(String projectId) {
        Map<String, String> param = new HashMap<>();
        param.put("project_id", projectId);
        return transform(RetrofitHelper.getIns()
                .COLLECT()
                .createMyCollection(param, RequestUtils.getHeaders(param, "", "")));
    }
}
