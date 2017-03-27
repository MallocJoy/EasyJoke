package com.utouu.easyjoke.module.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.flyco.tablayout.SlidingTabLayout;
import com.utouu.easyjoke.R;
import com.utouu.easyjoke.adapter.FragmentAdapter;
import com.utouu.easyjoke.base.BaseFragment;
import com.utouu.easyjoke.module.home.recomment.RecommendFragment;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Create by 黄思程 on 2016/12/15   17:41
 * Function：
 * Desc：
 */
public class HomeFragment extends BaseFragment {

    private final String[] mTitles3 = {"推荐", "视频", "段友秀", "图片", "段子", "精华", "同城", "游戏"};

    @BindView(R.id.stLayout)
    SlidingTabLayout stLayout;
    @BindView(R.id.vp_firstPage)
    ViewPager vpFirstPage;
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    @Override
    protected int getLayout() {
        return R.layout.layout_firstpage;
    }

    @Override
    protected void _initView(View view, Bundle bundle) {
        loadFragment();

        FragmentAdapter adapter = new FragmentAdapter(getChildFragmentManager(),mFragments);
        vpFirstPage.setAdapter(adapter);
        stLayout.setViewPager(vpFirstPage,mTitles3);
        vpFirstPage.setCurrentItem(0);
    }

    /**
     * 添加Fragment页面
     */
    private void loadFragment() {
        mFragments.add(new RecommendFragment());
        mFragments.add(new RecommendFragment());
        mFragments.add(new RecommendFragment());
        mFragments.add(new RecommendFragment());
        mFragments.add(new RecommendFragment());
        mFragments.add(new RecommendFragment());
        mFragments.add(new RecommendFragment());
        mFragments.add(new RecommendFragment());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFragments.clear();
    }
}
