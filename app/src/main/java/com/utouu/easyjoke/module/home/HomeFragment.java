package com.utouu.easyjoke.module.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.SlidingTabLayout;
import com.pacific.adapter.FragmentStatePagerAdapter2;
import com.utouu.easyjoke.R;
import com.utouu.easyjoke.RecommendFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by 黄思程 on 2016/12/15   17:41
 * Function：
 * Desc：
 */
public class HomeFragment extends Fragment {

    private final String[] mTitles3 = {"推荐", "视频", "段友秀", "图片", "段子", "精华", "同城", "游戏"};

    @BindView(R.id.stLayout)
    SlidingTabLayout stLayout;
    @BindView(R.id.vp_firstPage)
    ViewPager vpFirstPage;
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.layout_firstpage, container, false);
        ButterKnife.bind(this, rootView);

        initData();

        setAdapter();

        initSlidingTabLayout();

        return rootView;
    }

    private void initSlidingTabLayout() {
        stLayout.setViewPager(vpFirstPage,mTitles3);
        vpFirstPage.setCurrentItem(0);
    }

    private void setAdapter() {
        FragmentStatePagerAdapter2 adapter = new FragmentStatePagerAdapter2(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        };
        vpFirstPage.setAdapter(adapter);
        vpFirstPage.setCurrentItem(0);
    }

    private void initData() {
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
