package com.zy.mvp.gallery.presenter;

import android.os.Handler;
import android.os.Looper;

import com.zy.mvp.gallery.view.GalleryView;
import com.zy.mvp.gallery.widget.GalleryFragment;

import java.util.ArrayList;
import java.util.List;

public class GalleryPresenterImpl implements GalleryPresenter {
    private GalleryView mListView;
    private Handler handler;

    public GalleryPresenterImpl(GalleryView listView) {
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
        for (int i = 0; i < GalleryFragment.PAGE_SIZE; i++) {
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