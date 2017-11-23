package com.zy.mvp.share.presenter;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.zy.mvp.share.view.ShareView;
import com.zy.mvp.share.widget.ShareFragment;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SharePresenterImpl implements SharePresenter {
    private ShareView mListView;
    private Handler handler;

    public SharePresenterImpl(ShareView listView) {
        this.mListView = listView;
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void loadData(String token, final int page) {
        //只有第一页的或者刷新的时候才显示刷新进度条
// FIXME Rxjava 替换了下面8行
//        if (page == 1) {
// FIXME move to doOnSubscribe           mListView.showProgress();
//        }
//        List<String> list = new ArrayList<>();
//        for (int i = 0; i < ShareFragment.PAGE_SIZE; i++) {
//            list.add("item " + (i + 1));
//        }
//        success(list);
        Observable<List<String>> observable = Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(ObservableEmitter<List<String>> emitter) throws Exception {
                Log.e("sleep", "start");
                Thread.sleep(1000);//FIXME Rxjava模拟延迟加载
                Log.e("sleep", "end");
                List<String> list = new ArrayList<>();
                for (int i = 0; i < ShareFragment.PAGE_SIZE; i++) {
                    list.add("item " + (i + 1));
                }
                emitter.onNext(list);
                emitter.onComplete();
            }
        });
        success(observable, page);
    }

    private void success(Observable<List<String>> observable, final int page) {
        observable
                .subscribeOn(Schedulers.io())//设置可观察对象在Schedulers.io()的线程中发射数据
                // （用于IO密集型的操作，例如读写SD卡文件，查询数据库，访问网络等，
                // 具有线程缓存机制，在此调度器接收到任务后，先检查线程缓存池中，
                // 是否有空闲的线程，如果有，则复用，如果没有则创建新的线程，并加入到线程池中，如果每次都没有空闲线程使用，可以无上限的创建新线程）
                .observeOn(AndroidSchedulers.mainThread())//设置观察者在当前线程中接收数据
                // （在Android UI线程中执行任务，为Android开发定制）
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        if (page == 1) {
                            mListView.showProgress();
                        }
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mListView.hideProgress();
                    }
                })
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(@NonNull List<String> list) throws Exception {
                        Log.e("accept", "execute");
// FIXME move to doFinally                       mListView.hideProgress();
                        mListView.addData(list);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mListView.showLoadFailMsg("失败");
                        Log.e("Throwable", throwable.getMessage());
                    }
                });
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
        handler.post(new Runnable() {
            @Override
            public void run() {
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