package com.utouu.easyjoke.base;

import android.content.Context;
import android.util.Log;

import com.marno.easystatelibrary.EasyStatusView;
import com.utouu.android.commons.http.CheckLoginCallback;
import com.utouu.android.commons.utils.GsonUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import cn.utsoft.xunions.data.http.XunionsHttpUtils;
import cn.utsoft.xunions.entity.page.PageController;

/**
 * Created by 李波 on 2017/2/21.
 * Function:
 * Desc:
 */
public class BasePagePresenter<T> extends BasePresenter<IBasePageView<T>> implements IBaseStatusView, IBasePageView<T> {

    private static final int DEFAULT_SIZE = 10;

    private final PageController<T> mPageController;
    private final Context mContext;

    private int size = DEFAULT_SIZE;

    public BasePagePresenter(Context context) {
        this.mContext = context;
        this.mPageController = new PageController<>();
    }

    /**
     * 获取分页页码与size参数
     *
     * @return
     */
    public HashMap<String, String> getPageParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("page", mPageController.curSkip + "");
        params.put("size", size + "");
        return params;
    }

    /**
     * 获取下拉刷新参数
     *
     * @return
     */
    public HashMap<String, String> getRefreshParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("page", "0");
        params.put("size", size + "");
        return params;
    }

    /**
     * 发布下载成功, 保存分页数据
     *
     * @param controller
     */
    public void success(boolean loadMore, PageController<T> controller) {
        if (!loadMore) {
            mPageController.resetSkip();
        }
        mPageController.set(controller, size);

        onLoadFinish(true);

        IBasePageView<T> view = getView();
        if (view != null) {
            view.onDataBack(loadMore, controller.list);
            EasyStatusView statusView = view.getStatusView();
            if (statusView != null) {
                statusView.content();
            }
        }
        Log.e("page", mPageController.toString());
    }

    /**
     * 检查是否还有分页数据
     *
     * @return
     */
    public boolean checkMoreDate() {
        return mPageController.hasMoreDate();
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public void noNet() {
        EasyStatusView statusView = getStatusView();
        if (statusView != null) {
            statusView.noNet();
        }
    }

    @Override
    public void empty() {
        EasyStatusView statusView = getStatusView();
        if (statusView != null) {
            statusView.empty();
        }
    }

    @Override
    public void loading() {
        EasyStatusView statusView = getStatusView();
        if (statusView != null) {
            statusView.loading();
        }
    }

    @Override
    public void error(String msg) {
        EasyStatusView statusView = getStatusView();
        if (statusView != null) {
            statusView.error();
        }
    }

    @Override
    public void content() {
        EasyStatusView statusView = getStatusView();
        if (statusView != null) {
            statusView.content();
        }
    }

    /**
     * 发起请求
     *
     * @param url
     * @param params
     */
    protected void request(String url, HashMap<String, String> params, final Class<T> clazz, final boolean loadMore) {
        if (!hasDate()) {
            loading();
        }
        XunionsHttpUtils.loadData(mContext, url, params, new CheckLoginCallback() {

            @Override
            public void tgtInvalid(String message) {
                onLoadFinish(false);
                // 回调界面TGT失效
                tgtInvalid(message);
            }

            @Override
            public void success(String message) {
                Type type = new PageType(clazz);
                PageController<T> controller = GsonUtils.getGson().fromJson(message, type);
                if (controller == null || controller.isEmpty()) {
                    if (!hasDate()) {
                        empty();
                    }
                    onLoadFinish(false);
                } else {
                    BasePagePresenter.this.success(loadMore, controller);
                }
            }

            @Override
            public void failure(String message) {
                onLoadFinish(false);
                if (!hasDate()) {
                    empty();
                }
            }

            @Override
            public void failure(String statusCode, String message) {
                onLoadFinish(false);
                if (!hasDate()) {
                    error(message);
                }
            }
        });
    }

    @Deprecated
    @Override
    public void onDataBack(boolean loadMore, List<T> data) {
        IBasePageView<T> view = getView();
        if (view != null) {
            view.onDataBack(loadMore, data);
        }
    }

    @Deprecated
    @Override
    public void onLoadFinish(boolean success) {
        IBasePageView<T> view = getView();
        if (view != null) {
            view.onLoadFinish(success);
        }
    }

    @Deprecated
    @Override
    public void tgtInvalid(String message) {
        IBasePageView<T> view = getView();
        if (view != null) {
            view.tgtInvalid(message);
        }
    }

    @Deprecated
    @Override
    public boolean hasDate() {
        IBasePageView<T> view = getView();
        if (view != null) {
            return view.hasDate();
        }
        return false;
    }

    @Deprecated
    @Override
    public EasyStatusView getStatusView() {
        IBasePageView<T> view = getView();
        if (view != null) {
            EasyStatusView statusView = view.getStatusView();
            return statusView;
        }
        return null;
    }

    /**
     * Gson解析类型转换器
     */
    private static class PageType implements ParameterizedType {
        private Type type;

        private PageType(Type type) {
            this.type = type;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{type};
        }

        @Override
        public Type getRawType() {
            return PageController.class;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    }
}
