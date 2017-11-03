package com.lx.potato.mvp;

/**
 * Created by lixiang on 2017/11/2.
 */
public interface IMvpPresenter<V extends IMvpView> {
    void attachView(V view);

    void detachView();

    boolean isViewAttached();
}
