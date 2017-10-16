package com.zy.mvp.send.view;

import java.util.List;

public interface SendView {
    void showProgress();

    void addData(List<String> mData);

    void hideProgress();

    void showLoadFailMsg(String message);
}