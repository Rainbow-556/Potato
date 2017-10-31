package com.lx.potato.ui.activity;

import com.lx.potato.R;
import com.lx.potato.mvp.BaseMvpActivity;
import com.lx.potato.mvp.contract.HomeContract;
import com.lx.potato.presenter.HomePresenter;

/**
 * Created by lixiang on 2017/10/31.
 */
public class HomeActivity extends BaseMvpActivity<HomeContract.AbsHomePresenter> implements HomeContract.HomeView {
    @Override
    public void showData(String data) {
    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_home;
    }

    @Override
    protected HomeContract.AbsHomePresenter initPresenter() {
        HomePresenter presenter = new HomePresenter();
        presenter.attachView(this);
        return presenter;
    }

    @Override
    public void setPresenter(HomeContract.AbsHomePresenter presenter) {
    }
}
