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
                mMainView.switch2Camera();
                break;
            case R.id.nav_gallery:
                mMainView.switch2Gallery();
                break;
            case R.id.nav_share:
                mMainView.switch2Share();
                break;
            case R.id.nav_send:
                mMainView.switch2Send();
                break;
            default:
                mMainView.switch2Camera();
                break;
        }
    }
}