package com.lx.potato.mvp.contract;

import com.lx.potato.mvp.IMvpPresenter;
import com.lx.potato.mvp.IMvpView;

/**
 * Created by lixiang on 2017/10/31.
 */
public interface HomeContract {
    interface IHomeView extends IMvpView<IHomePresenter> {
        void showData(String data);
    }

    interface IHomePresenter extends IMvpPresenter<IHomeView> {
        void getData(String key);
    }
}
