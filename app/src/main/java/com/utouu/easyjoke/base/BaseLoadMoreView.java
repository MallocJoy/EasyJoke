package com.utouu.easyjoke.base;

import com.chad.library.adapter.base.loadmore.LoadMoreView;

import cn.utsoft.xunions.R;

/**
 * Created by cj on 2017/2/10.
 * Function:
 * Desc:
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
