package com.zy.mvp.camera.presenter;

import android.os.Handler;
import android.os.Looper;

import com.zy.mvp.camera.view.CameraView;
import com.zy.mvp.camera.widget.CameraFragment;

import java.util.ArrayList;
import java.util.List;

public class CameraPresenterImpl implements CameraPresenter {
    private CameraView mListView;
    private Handler handler;

    public CameraPresenterImpl(CameraView listView) {
        this.mListView = listView;
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void loadData(String token, final int page) {
        //只有第一页的或者刷新的时候才显示刷新进度条
        if (page == 1) {
            mListView.showProgress();
        }
        List<String> list = new ArrayList<>();
        for (int i = 0; i < CameraFragment.PAGE_SIZE; i++) {
            list.add("item " + (i + 1));
        }
        success(list);
    }

    private void success(final List<String> list) {
//TODO 模拟延迟加载
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                mListView.hideProgress();
//                mListView.addData(list);
//            }
//        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mListView.hideProgress();
                mListView.addData(list);
            }
        }, 1000);
    }

    private void failure(final String message) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                mListView.showLoadFailMsg(message);
            }
        });
    }
}