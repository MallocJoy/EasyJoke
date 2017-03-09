package com.utouu.easyjoke.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by cj on 2017/3/6.
 * Function:
 * Desc:
 */

public class NoFocusLinearLayout extends LinearLayout {
    public NoFocusLinearLayout(Context context) {
        super(context);
    }

    public NoFocusLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoFocusLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        this.requestDisallowInterceptTouchEvent(true);
//        return super.dispatchTouchEvent(ev);
//    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        float xDistance = 0, yDistance = 0;
        float xLast = 0 , yLast = 0;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();

                xDistance += Math.abs(curX - xLast);
                yDistance += Math.abs(curY - yLast);
                xLast = curX;
                yLast = curY;

                /**
                 * X轴滑动距离大于Y轴滑动距离，也就是用户横向滑动时，返回false，linearlayout不处理这次事件，
                 * 让子控件中的TouchEvent去处理，所以横向滑动的事件交由ViewPager处理，
                 */
                if (xDistance > yDistance) {
                    this.requestDisallowInterceptTouchEvent(true);
                    return false;
                }
        }

        return super.onInterceptTouchEvent(ev);
    }
}
