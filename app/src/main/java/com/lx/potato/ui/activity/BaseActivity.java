package com.lx.potato.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.lx.potato.mvp.IView;

/**
 * Created by lixiang on 2017/10/31.
 */
public abstract class BaseActivity extends AppCompatActivity implements IView {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        initView();
    }

    protected abstract void initView();

    protected abstract int getLayoutResId();

    @Override
    public void showLoading(boolean cancelable) {
    }

    @Override
    public void hideLoading() {
    }
}
