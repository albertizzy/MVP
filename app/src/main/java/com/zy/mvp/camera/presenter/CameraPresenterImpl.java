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
//FIXME Rxjava
//        List<String> list = new ArrayList<>();
//        for (int i = 0; i < CameraFragment.PAGE_SIZE; i++) {
//            list.add("item " + (i + 1));
//        }
//        success(list);
        Observable<List<String>> observable = Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(ObservableEmitter<List<String>> emitter) throws Exception {
                Log.e("sleep", "start");
                Thread.sleep(1000);//FIXME Rxjava模拟延迟加载，正式可删
                Log.e("sleep", "end");
                List<String> list = new ArrayList<>();
                for (int i = 0; i < CameraFragment.PAGE_SIZE; i++) {
                    list.add("item " + (i + 1));
                }
                emitter.onNext(list);
                emitter.onComplete();
            }
        });
        success(observable);
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

    private void success(Observable<List<String>> observable) {
        observable
                .subscribeOn(Schedulers.io())//设置可观察对象在Schedulers.io()的线程中发射数据
                // （用于IO密集型的操作，例如读写SD卡文件，查询数据库，访问网络等，
                // 具有线程缓存机制，在此调度器接收到任务后，先检查线程缓存池中，
                // 是否有空闲的线程，如果有，则复用，如果没有则创建新的线程，并加入到线程池中，如果每次都没有空闲线程使用，可以无上限的创建新线程）
                .observeOn(AndroidSchedulers.mainThread())//设置观察者在当前线程中接收数据
                // （在Android UI线程中执行任务，为Android开发定制）
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(@NonNull List<String> list) throws Exception {
                        Log.e("accept", "execute");
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