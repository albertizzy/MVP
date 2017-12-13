package com.zy.mvp.camera;

public interface CameraPresenter {
    void loadData(String token, int page);

    void unsubscribe();
}
