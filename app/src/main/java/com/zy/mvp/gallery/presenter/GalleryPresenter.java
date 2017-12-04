package com.zy.mvp.gallery.presenter;

public interface GalleryPresenter {
    void loadData(String token, int page);

    void unsubscribe();
}
