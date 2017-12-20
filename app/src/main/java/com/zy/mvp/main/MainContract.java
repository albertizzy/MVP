package com.zy.mvp.main;

interface MainContract {
    interface View {
        void switch2Camera(int id);

        void switch2Gallery(int id);

        void switch2Share(int id);

        void switch2Send(int id);
    }

    interface Presenter {
        void switchNavigation(int id);
    }
}