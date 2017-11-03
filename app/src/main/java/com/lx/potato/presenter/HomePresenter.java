package com.lx.potato.presenter;

import com.lx.potato.mvp.BaseMvpPresenter;
import com.lx.potato.mvp.contract.HomeContract;

/**
 * Created by lixiang on 2017/10/28.
 */
public class HomePresenter extends BaseMvpPresenter<HomeContract.IHomeView> implements HomeContract.IHomePresenter {
    @Override
    public void getData(String key) {

    }
}
