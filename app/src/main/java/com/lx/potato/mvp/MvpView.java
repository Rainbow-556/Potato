package com.lx.potato.mvp;

/**
 * Created by lixiang on 2017/10/31.
 */
public interface MvpView<P extends MvpPresenter> extends IView{
    void setPresenter(P presenter);
}
