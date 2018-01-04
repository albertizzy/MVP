package com.zy.mvp.tab;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zy.mvp.R;
import com.zy.mvp.camera.CameraFragment;

import java.util.ArrayList;
import java.util.List;

public class TabFragment extends Fragment {
    private static final String TAG = "TabFragment";
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach");
        //当Fragment与Activity发生关联时调用
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        //创建该Fragment的视图
        View view = inflater.inflate(R.layout.tab, null);
        TabLayout mTabLayout = view.findViewById(R.id.tab_layout);
        ViewPager mViewPager = view.findViewById(R.id.viewpager);
        mViewPager.setOffscreenPageLimit(3);
        setupViewPager(mViewPager);
        mTabLayout.setupWithViewPager(mViewPager);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated");
        //当Activity的onCreate方法返回时调用
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView");
        //与onCreateView相对应，当该Fragment的视图被移除时调用
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach");
        //与onAttach相对应，当Fragment与Activity关联被取消时调用
    }

    private void setupViewPager(ViewPager mViewPager) {
        //Fragment中嵌套使用Fragment一定要使用getChildFragmentManager(),否则会有问题
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getChildFragmentManager());
        if (fragment1 == null) {
            fragment1 = CameraFragment.newInstance("", true);
        }
        if (fragment2 == null) {
            fragment2 = CameraFragment.newInstance("", true);
        }
        if (fragment3 == null) {
            fragment3 = CameraFragment.newInstance("", true);
        }
        if (fragment4 == null) {
            fragment4 = CameraFragment.newInstance("", true);
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
