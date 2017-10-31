package com.lx.potato.mvp;

/**
 * Created by lixiang on 2017/10/31.
 */
public interface IView {
    void showLoading(boolean cancelable);

    void hideLoading();
}
