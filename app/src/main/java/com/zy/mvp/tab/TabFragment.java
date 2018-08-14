package com.zy.mvp.tab;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zy.mvp.R;
import com.zy.mvp.base.BaseFragment;
import com.zy.mvp.camera.CameraFragment;

import java.util.ArrayList;
import java.util.List;

public class TabFragment extends BaseFragment {
    public static TabFragment newInstance() {
        TabFragment fragment = new TabFragment();
        Bundle bundle = new Bundle();
//        bundle.putString("xxx", "xxx");
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //创建该Fragment的视图
        View view = inflater.inflate(R.layout.fragment_tab, null);
        TabLayout mTabLayout = view.findViewById(R.id.tab_layout);
        ViewPager mViewPager = view.findViewById(R.id.viewpager);
        mViewPager.setOffscreenPageLimit(3);
        //Fragment中嵌套使用Fragment一定要使用getChildFragmentManager(),否则会有问题
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getChildFragmentManager());
        adapter.addFragment(CameraFragment.newInstance("", true), "一");
        adapter.addFragment(CameraFragment.newInstance("", true), "二");
        adapter.addFragment(CameraFragment.newInstance("", true), "三");
        adapter.addFragment(CameraFragment.newInstance("", true), "四");
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        return view;
    }

    private static class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public MyFragmentPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}
