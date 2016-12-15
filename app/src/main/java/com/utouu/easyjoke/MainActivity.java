package com.utouu.easyjoke;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.pacific.adapter.Adapter;
import com.pacific.adapter.FragmentStatePagerAdapter2;
import com.pacific.adapter.PagerAdapterHelper;
import com.pacific.adapter.ViewPagerAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private String[] mTitles = {"首页", "发现", "新鲜", "消息"};
    private int[] mIconUnselectIds = {
            R.drawable.ic_tab_home_normal, R.drawable.ic_tab_discovery_normal,
            R.drawable.ic_tab_fresh_normal, R.drawable.ic_tab_msg_normal};
    private int[] mIconSelectIds = {
            R.drawable.ic_tab_home_pressed, R.drawable.ic_tab_discovery_pressed,
            R.drawable.ic_tab_fresh_pressed, R.drawable.ic_tab_msg_pressed};

    @BindView(R.id.viewPager) ViewPager viewPager;
    @BindView(R.id.ctLayout) CommonTabLayout ctLayout;

    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initData();

        initCommTabLayout();

        FragmentStatePagerAdapter2  adapter = new FragmentStatePagerAdapter2(
                getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        };

        viewPager.setAdapter(adapter);
    }

    private void initCommTabLayout() {

        ctLayout.setTabData(mTabEntities);
        ctLayout.setOnTabSelectListener(new OnTabSelectListener() {

            @Override  //关联ViewPager
            public void onTabSelect(int position) {
                viewPager.setCurrentItem(position);
            }

            @Override   //设置点击产生随机消息数
            public void onTabReselect(int position) {}});

        /**
         * ViewPager的滑动监听事件，与TabLayout相关联
         */
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ctLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        /**
         * 设置进入界面显示当前
         */
        viewPager.setCurrentItem(0);
    }

    private void initData() {

        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }

        mFragments.add(new SimpleFragment());
        mFragments.add(new SimpleFragment());
        mFragments.add(new SimpleFragment());
        mFragments.add(new SimpleFragment());
    }
}
