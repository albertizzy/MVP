package com.zy.mvp.send.presenter;

public interface SendPresenter {
    void loadData(String token, int page);

    void unsubscribe();
}
