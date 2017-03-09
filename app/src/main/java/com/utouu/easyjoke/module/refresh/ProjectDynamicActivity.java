package com.utouu.easyjoke.module.refresh;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aries.ui.view.title.TitleBarView;
import com.marno.easyutilcode.IntentUtil;
import com.utouu.easyjoke.module.refresh.presenter.ProjectDynamicPresenter;
import com.utouu.easyjoke.module.refresh.view.IProjectDynamicView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.bgabanner.BGABanner;
import cn.utsoft.xunions.R;
import cn.utsoft.xunions.base.BaseMVPActivity;
import cn.utsoft.xunions.entity.ProjectNewsEntity_HSC;
import cn.utsoft.xunions.entity.SpaceNewsEntity_HSC;
import cn.utsoft.xunions.manager.GlideManager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by chenxin on 2017/2/19.
 * Function:
 * Desc: 新闻详情页面
 */

public class ProjectDynamicActivity extends BaseMVPActivity<IProjectDynamicView,
        ProjectDynamicPresenter> implements IProjectDynamicView {

    @BindView(R.id.banner)
    BGABanner banner;
    @BindView(R.id.tv_dynamic_name)
    TextView mtvDynamicName;  //新闻标题
    @BindView(R.id.tv_dynamic_content)
    TextView mtvDynamicContent; //新闻内容
    @BindView(R.id.iv_news_detail)
    ImageView mivNewsPaly;  //视频播放图标

    /**
     * 项目动态/空间新闻的id
     */
    private String newId;
    /**
     * 项目的logo
     */
    private String logo;
    private int type;

    final List<String> images = new ArrayList<>();
    private ProjectDynamicPresenter dynamicPresenter;

    @Override
    protected ProjectDynamicPresenter createPresenter() {
        return new ProjectDynamicPresenter();
    }

    @Override
    protected void _initView(Bundle bundle) {
        dynamicPresenter = new ProjectDynamicPresenter();
        dynamicPresenter.attachView(this);
        banner.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        newId = intent.getStringExtra("newId");
        logo = intent.getStringExtra("logo");
        type = intent.getIntExtra("type", 0);

        if (0 == type)
            dynamicPresenter.requestProjectNews(this, newId);
        else
            dynamicPresenter.requestSpaceNews(this, newId);
    }

    private void setBanner(final String videoUrl) {

        banner.setAdapter(new BGABanner.Adapter() {
            @Override
            public void fillBannerItem(BGABanner banner, View view, Object model, int position) {
                GlideManager.loadImg(model, (ImageView) view);
            }
        });

        banner.setData(images, null);
        banner.setDelegate(new BGABanner.Delegate() {
            @Override
            public void onBannerItemClick(BGABanner banner, View itemView, Object model, int position) {
                if (!TextUtils.isEmpty(videoUrl) && 0 == position) {
                    JCVideoPlayerStandard.startFullscreen(mContext, JCVideoPlayerStandard.class, videoUrl, "");
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
            }
        });
        banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (!TextUtils.isEmpty(videoUrl) && mivNewsPaly != null) {
                    if (position == 0) {
                        ObjectAnimator.ofFloat(mivNewsPaly, "alpha", 0, 1).setDuration(500).start();
                        mivNewsPaly.setVisibility(View.VISIBLE);
                    } else {
                        ObjectAnimator.ofFloat(mivNewsPaly, "alpha", 1, 0).setDuration(300).start();
                        mivNewsPaly.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (!TextUtils.isEmpty(videoUrl) && mivNewsPaly != null) {
                    if (position == 0) {
                        mivNewsPaly.setVisibility(View.VISIBLE);
                    } else {
                        mivNewsPaly.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_project_dynamic;
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        super.onResume();
    }

    @Override
    protected void setTitleBar(TitleBarView titleBar) {
        super.setTitleBar(titleBar);
        titleBar.setTitleMainText("项目动态");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    /**
     * 跳转新闻详情页面
     *
     * @param context 上下文
     * @param newId   新闻id/项目动态newId
     * @param logo    项目logo url地址
     * @param type    0为项目动态id，1位空间动态id
     */
    public static void startNewsActivity(Context context, String newId, String logo, int type) {
        Intent intent = new Intent(context, ProjectDynamicActivity.class);
        intent.putExtra("newId", newId);
        intent.putExtra("logo", logo);
        intent.putExtra("type", type);
        IntentUtil.to(context, intent);
    }

    @Override
    public void onGetProjectNewsSuccess(ProjectNewsEntity_HSC entity) {
        //设置新闻标题和内容
        mtvDynamicName.setText(entity.title);
        mtvDynamicContent.setText(entity.describes);

        //添加Banner展示的图片的url
        images.clear();
        if (!TextUtils.isEmpty(entity.newsVideo)) {
            images.add(logo);
            mivNewsPaly.setVisibility(View.VISIBLE);
        }
        if (entity.photos != null && !entity.photos.isEmpty()) {
            images.addAll(entity.photos);
        }

        //如果图片和视频都没有则隐藏Banner,否则显示Banner
        if (TextUtils.isEmpty(entity.newsVideo) && (entity.photos == null || entity.photos.size() == 0))
            banner.setVisibility(View.GONE);
        else {
            setBanner(entity.newsVideo);
            banner.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onGetSpaceNewsSuccess(SpaceNewsEntity_HSC entity) {
        //设置新闻标题和内容
        mtvDynamicName.setText(entity.title);
        mtvDynamicContent.setText(entity.description);

        //添加Banner展示的图片的url
        images.clear();
        if (!TextUtils.isEmpty(entity.video)) {
            images.add(logo);
            mivNewsPaly.setVisibility(View.VISIBLE);
        }
        if (entity.pictures != null && !entity.pictures.isEmpty()) {
            images.addAll(entity.pictures);
        }

        //如果图片和视频都没有则隐藏Banner,否则显示Banner
        if (TextUtils.isEmpty(entity.video) && (entity.pictures == null || entity.pictures.size() == 0))
            banner.setVisibility(View.GONE);
        else {
            setBanner(entity.video);
            banner.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void handleFailed(String message) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dynamicPresenter.detacheView();
    }
}
