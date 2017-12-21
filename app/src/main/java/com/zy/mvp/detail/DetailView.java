package com.zy.mvp.detail;

interface DetailView {
    void showDetailContent(String detailContent);

    void showProgress();

    void hideProgress();
}
