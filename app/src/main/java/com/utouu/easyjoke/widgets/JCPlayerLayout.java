package com.utouu.easyjoke.widgets;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.AttributeSet;
import android.view.View;

import cn.utsoft.xunions.R;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by 李良海 on 2017/2/20 9:52.
 * Function: 视频播放控件
 * Desc:
 */

public class JCPlayerLayout extends JCVideoPlayerStandard {

    public JCPlayerLayout(Context context) {
        super(context);
    }

    public JCPlayerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //
    @Override
    public int getLayoutId() {
        return R.layout.layout_detail_player;
    }

    @Override
    public void updateStartImage() {

        if (currentState == CURRENT_STATE_PLAYING) {
            startButton.setImageResource(R.drawable.ic_detail_pause);
        } else if (currentState == CURRENT_STATE_ERROR) {
            startButton.setImageResource(fm.jiecao.jcvideoplayer_lib.R.drawable.jc_click_error_selector);
        } else {
            startButton.setImageResource(R.drawable.ic_detail_play);
        }
    }

    /**
     * 播放完成退出activity
     * @param state
     */
    @Override
    public void setUiWitStateAndScreen(int state) {
        if (state == CURRENT_STATE_AUTO_COMPLETE) {
            JCVideoPlayer.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            JCVideoPlayer.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        }
        super.setUiWitStateAndScreen(state);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fullscreen) {
            if (currentScreen == SCREEN_LAYOUT_NORMAL) {
                JCVideoPlayer.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                JCVideoPlayer.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            } else if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
                JCVideoPlayer.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                JCVideoPlayer.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            }
        } else if (v.getId() == R.id.back) {
            if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
                JCVideoPlayer.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                JCVideoPlayer.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            }
        }
        super.onClick(v);
    }
}
