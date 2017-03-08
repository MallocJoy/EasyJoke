package com.utouu.easyjoke.module.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.pacific.adapter.FragmentStatePagerAdapter2;
import com.utouu.easyjoke.R;
import com.utouu.easyjoke.entity.main.TabEntity;
import com.utouu.easyjoke.module.discover.DiscoverFragment;
import com.utouu.easyjoke.module.home.HomeFragment;
import com.utouu.easyjoke.module.message.MessageFragment;
import com.utouu.easyjoke.module.refresh.RefreshFragment;
import com.utouu.easyjoke.widgets.NoScrollViewPager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private String[] mTitles = {"首页", "发现", "新鲜", "消息"};
    private String[] mTitles2 = {"精选","关注"};
    private String[] mTitles3 = {"热吧","订阅"};
    private String[] mTitles4 = {"新鲜"};
    private String[] mTitles5 = {"消息"};
    private int[] mIconUnselectIds = {
            R.drawable.ic_tab_home_normal, R.drawable.ic_tab_discovery_normal,
            R.drawable.ic_tab_fresh_normal, R.drawable.ic_tab_msg_normal};
    private int[] mIconSelectIds = {
            R.drawable.ic_tab_home_pressed, R.drawable.ic_tab_discovery_pressed,
            R.drawable.ic_tab_fresh_pressed, R.drawable.ic_tab_msg_pressed};

    @BindView(R.id.viewPager)
    NoScrollViewPager viewPager;
    @BindView(R.id.iv_head) ImageView ivHead;
    @BindView(R.id.iv_publish) ImageView ivPublish;

    @BindView(R.id.stLayout) SegmentTabLayout stLayout;
    @BindView(R.id.ctLayout) CommonTabLayout ctLayout;

    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    private FragmentStatePagerAdapter2 adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initData();

        initCommTabLayout();

        setAdapter();
    }

    private void setAdapter() {
        adapter = new FragmentStatePagerAdapter2(
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
        viewPager.setCanScrollble(false);
        viewPager.setAdapter(adapter);
    }

    private void initCommTabLayout() {
        stLayout.setTabData(mTitles2);
        ctLayout.setTabData(mTabEntities);
        ctLayout.setOnTabSelectListener(new OnTabSelectListener() {

            @Override  //关联ViewPager
            public void onTabSelect(int position) {
                viewPager.setCurrentItem(position);

                if (position ==0){
                    stLayout.setTabData(mTitles2);
                    ivHead.setImageResource(R.drawable.default_round_head);
                    ivPublish.setImageResource(R.drawable.ic_publish);
                }else if (position ==1){
                    stLayout.setTabData(mTitles3);
                    ivHead.setImageResource(R.drawable.ic_discovery_search);
                    ivPublish.setImageResource(R.drawable.ic_nearby);
                }else if (position == 2){
                    stLayout.setTabData(mTitles4);
                }else {
                    stLayout.setTabData(mTitles5);
                }
            }

            @Override   //设置点击产生随机消息数
            public void onTabReselect(int position) {
            }
        });
        viewPager.setCurrentItem(0);
    }

    private void initData() {

        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        mFragments.add(new HomeFragment());
        mFragments.add(new DiscoverFragment());
        mFragments.add(new RefreshFragment());
        mFragments.add(new MessageFragment());
    }
}
