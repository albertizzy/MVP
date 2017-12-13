package com.zy.mvp.gallery;

import java.util.List;

public interface GalleryContract {
    interface View {
        void showProgress();

        void addData(List<String> mData);

        void hideProgress();

        void showLoadFailMsg(String message);
    }

    interface Presenter {
        void loadData(String token, int page);

        void unsubscribe();
    }
}