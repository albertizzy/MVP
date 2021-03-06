package com.zy.mvp.send;

import java.util.List;

interface SendContract {
    interface View {
        void showProgress();

        void addData(List<String> data);

        void hideProgress();

        void showLoadFailMsg(String message);
    }

    interface Presenter {
        void loadData(String token, int page);

        void unSubscribe();
    }
}