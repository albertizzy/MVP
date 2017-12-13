package com.zy.mvp.send;

public interface SendPresenter {
    void loadData(String token, int page);

    void unsubscribe();
}
