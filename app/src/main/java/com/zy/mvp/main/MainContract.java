package com.zy.mvp.main;

interface MainContract {
    interface View {
        void switch2Camera();

        void switch2Gallery();

        void switch2Share();

        void switch2Send();
    }

    interface Presenter {
        void switchNavigation(int id);
    }
}