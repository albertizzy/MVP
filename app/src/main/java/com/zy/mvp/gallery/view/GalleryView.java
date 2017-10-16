package com.zy.mvp.gallery.view;

import java.util.List;

public interface GalleryView {
    void showProgress();

    void addData(List<String> mData);

    void hideProgress();

    void showLoadFailMsg(String message);
}