package com.lx.potato.mvp;

/**
 * Created by lixiang on 2017/10/31.
 */
public interface IMvpView<P extends IMvpPresenter> extends IView {
    void setPresenter(P presenter);
}
