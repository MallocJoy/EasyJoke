package com.utouu.easyjoke;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Create by 黄思程 on 2016/12/16   10:55
 * Function： 禁止左右滑动的ViewPager
 * Desc： 通过设置  setCanScrollable方法来实现
 */
public class NoScrollViewPager extends ViewPager{

    private boolean mScrollble = true;


    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!mScrollble) {
            return false;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!mScrollble) {
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }

    public boolean isCanScrollble() {
        return mScrollble;
    }

    public void setCanScrollble(boolean scrollble) {
        this.mScrollble = scrollble;
    }
}
