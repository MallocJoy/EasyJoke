package com.utouu.easyjoke.base;

import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.utouu.easyjoke.R;

/**
 * Create by 黄思程 on 2017/3/20  9:45
 * Function：
 * Desc：点击加载更多视图
 */
public class BaseLoadMoreView extends LoadMoreView {
    @Override
    public int getLayoutId() {
        return R.layout.layout_loadmore_view;
    }

    @Override
    protected int getLoadingViewId() {
        return 0;
    }

    @Override
    protected int getLoadFailViewId() {
        return 0;
    }

    @Override
    protected int getLoadEndViewId() {
        return 0;
    }
}
