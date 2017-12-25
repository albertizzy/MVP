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
import com.zy.mvp.camera.CameraFragment;

import java.util.ArrayList;
import java.util.List;

public class TabFragment extends Fragment {
    private CameraFragment fragment1;
    private CameraFragment fragment2;
    private CameraFragment fragment3;
    private CameraFragment fragment4;

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
        View view = inflater.inflate(R.layout.tab, null);
        TabLayout mTabLayout = view.findViewById(R.id.tab_layout);
        ViewPager mViewPager = view.findViewById(R.id.viewpager);
        mViewPager.setOffscreenPageLimit(3);
        setupViewPager(mViewPager);
        mTabLayout.addTab(mTabLayout.newTab().setText("一"));
        mTabLayout.addTab(mTabLayout.newTab().setText("二"));
        mTabLayout.addTab(mTabLayout.newTab().setText("三"));
        mTabLayout.addTab(mTabLayout.newTab().setText("四"));
        mTabLayout.setupWithViewPager(mViewPager);
        return view;
    }

    private void setupViewPager(ViewPager mViewPager) {
        //Fragment中嵌套使用Fragment一定要使用getChildFragmentManager(),否则会有问题
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getChildFragmentManager());
        if (fragment1 == null) {
            fragment1 = CameraFragment.newInstance("");
            fragment1.isShowFooter(true);
        }
        if (fragment2 == null) {
            fragment2 = CameraFragment.newInstance("");
            fragment2.isShowFooter(true);
        }
        if (fragment3 == null) {
            fragment3 = CameraFragment.newInstance("");
            fragment3.isShowFooter(true);
        }
        if (fragment4 == null) {
            fragment4 = CameraFragment.newInstance("");
            fragment4.isShowFooter(true);
        }
        adapter.addFragment(fragment1, "一");
        adapter.addFragment(fragment2, "二");
        adapter.addFragment(fragment3, "三");
        adapter.addFragment(fragment4, "四");
        mViewPager.setAdapter(adapter);
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
