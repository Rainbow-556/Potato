package com.lx.potato.mvp.contract;

import com.lx.potato.mvp.MvpPresenter;
import com.lx.potato.mvp.MvpView;

/**
 * Created by lixiang on 2017/10/31.
 */
public interface HomeContract {
    interface HomeView extends MvpView<AbsHomePresenter> {
        void showData(String data);
    }

    abstract class AbsHomePresenter extends MvpPresenter<HomeView> {
        public abstract void getData(String key);
    }
}
