package com.utouu.easyjoke.util;

import android.animation.ObjectAnimator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;

/**
 * Created by llh on 2017/2/26 09:55.
 * Function: 进度条设置进度时添加进度从0到指定值的动画
 * Desc:
 */

public class ProgressBarUtils {

    /**
     * 设置进度值动画
     */
    public static void setProgressAnim(ProgressBar mProgressBar1, int progress, long duration) {
        setProgressAnim(mProgressBar1, progress, duration, false);
    }

    public static void setProgressAnim(ProgressBar mProgressBar1, int progress, long duration, boolean clear) {
        if (mProgressBar1.getAnimation() != null) {
            mProgressBar1.clearAnimation();
        }
        if (clear) {
            mProgressBar1.setProgress(0);
        }
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofInt(mProgressBar1, "progress", progress);
        objectAnimator1.setDuration(duration);
        objectAnimator1.setInterpolator(new DecelerateInterpolator());
        objectAnimator1.start();
    }
}
