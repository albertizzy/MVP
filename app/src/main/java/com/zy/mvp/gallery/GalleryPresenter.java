package com.zy.mvp.gallery;

public interface GalleryPresenter {
    void loadData(String token, int page);

    void unsubscribe();
}
