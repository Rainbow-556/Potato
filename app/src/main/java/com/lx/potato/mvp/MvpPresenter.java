package com.lx.potato.mvp;

/**
 * Created by lixiang on 2017/10/31.
 */
public abstract class MvpPresenter<V extends MvpView> {
    protected V mView;

    public void attachView(V view) {
        this.mView = view;
    }

    public void detachView() {
        this.mView = null;
    }

    protected boolean isViewAttached() {
        return mView != null;
    }
}
