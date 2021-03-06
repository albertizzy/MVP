package com.zy.mvp.camera;

import java.util.List;

interface CameraContract {
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