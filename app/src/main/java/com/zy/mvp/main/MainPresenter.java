package com.zy.mvp.main;

import com.zy.mvp.R;

public class MainPresenter implements MainContract.Presenter {
    private final MainContract.View mMainView;

    public MainPresenter(MainContract.View mainView) {
        this.mMainView = mainView;
    }

    @Override
    public void switchNavigation(int id) {
        switch (id) {
            case R.id.nav_camera:
                mMainView.switch2Camera(id);
                break;
            case R.id.nav_gallery:
                mMainView.switch2Gallery(id);
                break;
            case R.id.nav_share:
                mMainView.switch2Share(id);
                break;
            case R.id.nav_send:
                mMainView.switch2Send(id);
                break;
            default:
                mMainView.switch2Camera(id);
                break;
        }
    }
}