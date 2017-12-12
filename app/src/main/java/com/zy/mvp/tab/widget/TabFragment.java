package com.zy.mvp.tab.widget;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zy.mvp.R;
import com.zy.mvp.camera.widget.CameraFragment;

public class TabFragment extends Fragment {
    private TabLayout mTablayout;
    private ViewPager mViewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab, null);
        mTablayout = view.findViewById(R.id.tab_layout);
        mViewPager = view.findViewById(R.id.viewpager);
        mViewPager.setOffscreenPageLimit(3);
        setupViewPager(mViewPager);
        mTablayout.addTab(mTablayout.newTab().setText("一"));
        mTablayout.addTab(mTablayout.newTab().setText("二"));
        mTablayout.addTab(mTablayout.newTab().setText("三"));
        mTablayout.addTab(mTablayout.newTab().setText("四"));
        mTablayout.setupWithViewPager(mViewPager);
        return view;
    }

    private void setupViewPager(ViewPager mViewPager) {
        //Fragment中嵌套使用Fragment一定要使用getChildFragmentManager(),否则会有问题
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getChildFragmentManager());
        adapter.addFragment(CameraFragment.newInstance("", true), "一");
        adapter.addFragment(CameraFragment.newInstance("", true), "二");
        adapter.addFragment(CameraFragment.newInstance("", true), "三");
        adapter.addFragment(CameraFragment.newInstance("", true), "四");
        mViewPager.setAdapter(adapter);
    }
}
