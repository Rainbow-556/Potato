package com.lx.potato.ui.activity;

import com.lx.potato.R;
import com.lx.potato.mvp.BaseMvpActivity;
import com.lx.potato.mvp.contract.HomeContract;

/**
 * Created by lixiang on 2017/10/31.
 */
public class HomeActivity extends BaseMvpActivity<HomeContract.IHomePresenter> implements HomeContract.IHomeView {

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_home;
    }

    @Override
    public void setPresenter(HomeContract.IHomePresenter presenter) {
    }

    @Override
    public void showData(String data) {
    }

    @Override
    protected HomeContract.IHomePresenter initPresenter() {
        return null;
    }
}
