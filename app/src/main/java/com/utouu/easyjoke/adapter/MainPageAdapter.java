package com.utouu.easyjoke.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.pacific.adapter.FragmentStatePagerAdapter2;

import java.util.List;

/**
 * Create by 黄思程 on 2017/3/21   10:36
 * Function：
 * Desc：主页面数据的adapter
 */
public class MainPageAdapter extends FragmentStatePagerAdapter2 {

    List<Fragment> fragments;

    public MainPageAdapter(FragmentManager fm,List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
