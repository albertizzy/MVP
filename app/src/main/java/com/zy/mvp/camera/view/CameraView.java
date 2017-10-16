package com.zy.mvp.camera.view;

import java.util.List;

public interface CameraView {
    void showProgress();

    void addData(List<String> mData);

    void hideProgress();

    void showLoadFailMsg(String message);
}