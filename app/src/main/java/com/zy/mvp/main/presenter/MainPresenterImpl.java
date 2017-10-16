package com.zy.mvp.main.presenter;

import com.zy.mvp.R;
import com.zy.mvp.main.view.MainView;

public class MainPresenterImpl implements MainPresenter {
    private MainView mMainView;

    public MainPresenterImpl(MainView mainView) {
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