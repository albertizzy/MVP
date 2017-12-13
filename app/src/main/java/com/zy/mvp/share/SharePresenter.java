package com.zy.mvp.share;

public interface SharePresenter {
    void loadData(String token, int page);

    void unsubscribe();
}
