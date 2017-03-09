package com.utouu.easyjoke.util;

import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by cj on 2017/2/26.
 * Function:修改进度条和进度字体颜色的工具类
 * Desc:
 */

public class ProgressBarStyleUtil {
    public static void setStyle(int progress, ProgressBar progressBar, TextView textView) {
        int color = 0;
        if (progress < 1){
            color = Color.parseColor("#1a000000");
        }else if (progress < 25 && progress >= 1) {
            color = Color.parseColor("#55C2DF");
        } else if (progress < 50) {
            color = Color.parseColor("#6666FF");
        } else if (progress < 75) {
            color = Color.parseColor("#FF9C1F");
        } else {
            color = Color.parseColor("#FF2E64");
        }

        if (progressBar != null) {
            ClipDrawable drawable = new ClipDrawable(new ColorDrawable(color), Gravity.LEFT, ClipDrawable.HORIZONTAL);
            progressBar.setProgressDrawable(drawable);
            drawable.setLevel(progress * 100);
            progressBar.setProgressDrawable(drawable);
            progressBar.setProgress(progress);
        }

//        ClipDrawable drawable = (ClipDrawable) progressBar.getProgressDrawable();
        if (textView != null) {
            textView.setTextColor(color);
            textView.setText(progress + "%");
        }
    }
}
