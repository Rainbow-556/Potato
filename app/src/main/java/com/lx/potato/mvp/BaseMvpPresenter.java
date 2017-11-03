package com.lx.potato.mvp;

/**
 * Created by lixiang on 2017/10/31.
 */
public class BaseMvpPresenter<V extends IMvpView> implements IMvpPresenter<V> {
    protected V mView;

    @Override
    public void attachView(V view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        this.mView = null;
    }

    @Override
    public boolean isViewAttached() {
        return this.mView != null;
    }
}
