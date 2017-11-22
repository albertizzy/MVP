package com.zy.mvp.camera.presenter;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.zy.mvp.camera.view.CameraView;
import com.zy.mvp.camera.widget.CameraFragment;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

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
//TODO 正常加载
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                mListView.hideProgress();
//                mListView.addData(list);
//            }
//        });
// TODO 模拟延迟加载
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mListView.hideProgress();
//                mListView.addData(list);
//            }
//        }, 1000);
//FIXME Rxjava
        Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(ObservableEmitter<List<String>> emitter) throws Exception {
                Thread.sleep(1000);//FIXME Rxjava模拟延迟加载，正式可删
                emitter.onNext(list);
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())//设置可观察对象在Schedulers.io()的线程中发射数据
                .observeOn(AndroidSchedulers.mainThread())//设置观察者在当前线程中接收数据
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(@NonNull List<String> list) throws Exception {
                        Log.e("sdf", "asdasd");
                        mListView.hideProgress();
                        mListView.addData(list);
                    }
                });
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