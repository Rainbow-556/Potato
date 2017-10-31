package com.lx.potato.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.lx.potato.ui.activity.BaseActivity;

/**
 * Created by lixiang on 2017/10/31.
 */
public abstract class BaseMvpActivity<P extends MvpPresenter> extends BaseActivity {
    protected P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = initPresenter();
    }

    protected abstract P initPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
