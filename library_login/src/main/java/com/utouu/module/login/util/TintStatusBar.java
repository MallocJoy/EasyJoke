package com.utouu.module.login.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

/**
 * Created: AriesHoo on 2017-02-27 13:23
 * Function:
 * Desc:
 */
public class TintStatusBar {

    private static final int COLOR_TRANSLUCENT = Color.parseColor("#00000000");
    public static final int DEFAULT_COLOR_ALPHA = 112;

    public TintStatusBar() {
    }

    public static void setStatusBarColor(Activity activity, int statusColor, int alpha) {
        setStatusBarColor(activity, calculateStatusBarColor(statusColor, alpha));
    }

    public static void setStatusBarColor(Activity activity, int statusColor) {
        Window window = activity.getWindow();
        ViewGroup mContentView = (ViewGroup) activity.findViewById(android.R.id.content);
        if (Build.VERSION.SDK_INT >= 19) {
            window.addFlags(67108864);
            if (Build.VERSION.SDK_INT >= 21) {
                window.clearFlags(67108864);
                window.addFlags(-2147483648);
                window.setStatusBarColor(statusColor);
                View mDecorView = mContentView.getChildAt(0);
                if (mDecorView != null) {
                    ViewCompat.setFitsSystemWindows(mDecorView, true);
                }
            } else {
                ViewGroup mDecorView1 = (ViewGroup) window.getDecorView();
                if (mDecorView1.getTag() != null && mDecorView1.getTag() instanceof Boolean && ((Boolean) mDecorView1.getTag()).booleanValue()) {
                    View statusBarHeight1 = mDecorView1.getChildAt(0);
                    if (statusBarHeight1 != null) {
                        statusBarHeight1.setBackgroundColor(statusColor);
                    }
                } else {
                    int statusBarHeight = getStatusBarHeight(activity);
                    View mContentChild = mContentView.getChildAt(0);
                    if (mContentChild != null) {
                        ViewCompat.setFitsSystemWindows(mContentChild, false);
                        FrameLayout.LayoutParams mStatusBarView = (FrameLayout.LayoutParams) mContentChild.getLayoutParams();
                        mStatusBarView.topMargin += statusBarHeight;
                        mContentChild.setLayoutParams(mStatusBarView);
                    }

                    View mStatusBarView1 = new View(activity);
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, statusBarHeight);
                    layoutParams.gravity = 48;
                    mStatusBarView1.setLayoutParams(layoutParams);
                    mStatusBarView1.setBackgroundColor(statusColor);
                    mDecorView1.addView(mStatusBarView1, 0);
                    mDecorView1.setTag(Boolean.valueOf(true));
                }
            }
        }

    }

    public static void translucentStatusBar(Activity activity) {
        translucentStatusBar(activity, false);
    }

    public static void translucentStatusBar(Activity activity, boolean hideStatusBarBackground) {
        Window window = activity.getWindow();
        ViewGroup mContentView = (ViewGroup) activity.findViewById(16908290);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, false);
        }

        if (Build.VERSION.SDK_INT >= 19) {
            int statusBarHeight = getStatusBarHeight(activity);
            window.addFlags(67108864);
            if (Build.VERSION.SDK_INT >= 21) {
                window.addFlags(-2147483648);
                if (hideStatusBarBackground) {
                    window.clearFlags(67108864);
                    window.setStatusBarColor(COLOR_TRANSLUCENT);
                } else {
                    window.setStatusBarColor(calculateStatusBarColor(COLOR_TRANSLUCENT, 112));
                }

                if (mChildView != null) {
                    ViewCompat.requestApplyInsets(mChildView);
                }
            } else {
                ViewGroup mDecorView = (ViewGroup) window.getDecorView();
                if (mDecorView.getTag() != null && mDecorView.getTag() instanceof Boolean && ((Boolean) mDecorView.getTag()).booleanValue()) {
                    mChildView = mDecorView.getChildAt(0);
                    mContentView.removeView(mChildView);
                    mChildView = mContentView.getChildAt(0);
                    if (mChildView != null) {
                        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mChildView.getLayoutParams();
                        if (lp != null && lp.topMargin >= statusBarHeight) {
                            lp.topMargin -= statusBarHeight;
                            mChildView.setLayoutParams(lp);
                        }
                    }

                    mDecorView.setTag(Boolean.valueOf(false));
                }
            }
        }

    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            result = context.getResources().getDimensionPixelOffset(resId);
        }

        return result;
    }

    private static int calculateStatusBarColor(int color, int alpha) {
        float a = 1.0F - (float) alpha / 255.0F;
        int red = color >> 16 & 255;
        int green = color >> 8 & 255;
        int blue = color & 255;
        red = (int) ((double) ((float) red * a) + 0.5D);
        green = (int) ((double) ((float) green * a) + 0.5D);
        blue = (int) ((double) ((float) blue * a) + 0.5D);
        return -16777216 | red << 16 | green << 8 | blue;
    }

}
