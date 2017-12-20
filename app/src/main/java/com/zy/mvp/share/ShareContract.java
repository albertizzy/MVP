package com.zy.mvp.share;

import java.util.List;

interface ShareContract {
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