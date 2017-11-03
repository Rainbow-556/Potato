package com.lx.potato.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.lx.potato.ui.fragment.BaseFragment;

/**
 * Created by lixiang on 2017/11/2.
 */
public abstract class BaseMvpFragment<P extends IMvpPresenter> extends BaseFragment {
    protected P mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = initPresenter();
    }

    protected abstract P initPresenter();

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
