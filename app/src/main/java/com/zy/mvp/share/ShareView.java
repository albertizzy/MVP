package com.zy.mvp.share;

import java.util.List;

public interface ShareView {
    void showProgress();

    void addData(List<String> mData);

    void hideProgress();

    void showLoadFailMsg(String message);
}