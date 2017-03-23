package com.utouu.easyjoke.module.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.orhanobut.logger.Logger;
import com.utouu.easyjoke.R;
import com.utouu.easyjoke.adapter.MainPageAdapter;
import com.utouu.easyjoke.base.BaseMVPActivity;
import com.utouu.easyjoke.entity.BaseEntity;
import com.utouu.easyjoke.entity.main.MainEntity;
import com.utouu.easyjoke.entity.main.TabEntity;
import com.utouu.easyjoke.module.discover.DiscoverFragment;
import com.utouu.easyjoke.module.home.HomeFragment;
import com.utouu.easyjoke.module.main.presenter.MainPresenter;
import com.utouu.easyjoke.module.main.view.IMainView;
import com.utouu.easyjoke.module.message.MessageFragment;
import com.utouu.easyjoke.module.refresh.RefreshFragment;
import com.utouu.easyjoke.widgets.NoScrollViewPager;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Create by 黄思程 on 2017/3/21  13:03
 * Function：
 * Desc：主界面数据设置
 */
public class MainActivity extends BaseMVPActivity<IMainView, MainPresenter>
        implements IMainView, OnTabSelectListener {

    @BindView(R.id.viewPager)
    NoScrollViewPager viewPager;
    @BindView(R.id.ctLayout)
    CommonTabLayout ctLayout;

    private static final String[] mTitles = {"首页", "发现", "新鲜", "消息"};
    private static final String[] mTitles2 = {"精选", "关注"};
    private static final String[] mTitles3 = {"热吧", "订阅"};

    private static final int[] mIconUnselectIds = {
            R.drawable.ic_tab_home_normal, R.drawable.ic_tab_discovery_normal,
            R.drawable.ic_tab_fresh_normal, R.drawable.ic_tab_msg_normal};
    private static final int[] mIconSelectIds = {
            R.drawable.ic_tab_home_pressed, R.drawable.ic_tab_discovery_pressed,
            R.drawable.ic_tab_fresh_pressed, R.drawable.ic_tab_msg_pressed};


    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private MainPageAdapter adapter;

    @Override
    protected void _initView(Bundle bundle) {
        mPresenter.attachView(this);
        mPresenter.requestMainPage();

        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        mFragments.add(new HomeFragment());
        mFragments.add(new DiscoverFragment());
        mFragments.add(new RefreshFragment());
        mFragments.add(new MessageFragment());

        ctLayout.setTabData(mTabEntities);
        titleSegment.setTabData(mTitles2);

        adapter = new MainPageAdapter(getSupportFragmentManager(), mFragments);
        viewPager.setCanScrollble(false);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);

        ctLayout.setOnTabSelectListener(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter();
    }

    @Override
    public void onGetMainPageSuccess(BaseEntity<MainEntity> entity) {
        Logger.d(entity.toString());
    }

    @Override
    public void onFailed(String message) {
        Logger.e(message);
    }

    @Override
    public void onComplete(String message) {
        Logger.d(message);
    }

    @Override
    public void onTabSelect(int position) {
        switch (position) {
            case 0:
            default:
                titleSegment.setVisibility(View.VISIBLE);
                titleText.setLeftIcon(getResources().getDrawable(R.drawable.default_round_head));
                titleText.setRightIcon(getResources().getDrawable(R.drawable.ic_publish));
                titleText.setRightString("");
                titleSegment.setTabData(mTitles2);
                break;
            case 1:
                titleSegment.setVisibility(View.VISIBLE);
                titleText.setLeftIcon(getResources().getDrawable(R.drawable.ic_discovery_search));
                titleText.setRightIcon(getResources().getDrawable(R.drawable.ic_nearby));
                titleText.setRightString("");
                titleSegment.setTabData(mTitles3);
                break;
            case 2:
                titleSegment.setVisibility(View.GONE);
                titleText.setLeftIcon(getResources().getDrawable(R.drawable.default_round_head));
                titleText.setRightIcon(getResources().getDrawable(R.drawable.ic_publish));
                titleText.setRightString("");
                titleText.setCenterString("新鲜");
                break;
            case 3:
                titleSegment.setVisibility(View.GONE);
                titleText.setLeftIcon(getResources().getDrawable(R.drawable.default_round_head));
                titleText.setRightIcon(null);
                titleText.setRightString("黑名单");
                titleText.setCenterString("消息");
                break;
        }
    }

    @Override
    public void onTabReselect(int position) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detacheView();
    }
}
