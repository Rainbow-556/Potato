package com.lx.potato.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lx.potato.mvp.IView;
import com.lx.potato.ui.activity.BaseActivity;

/**
 * Created by lixiang on 2017/10/31.
 */
public abstract class BaseFragment extends Fragment  implements IView {
    protected BaseActivity mActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutResId(), container, false);
    }

    public boolean isAttached() {
        return mActivity != null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (BaseActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    protected abstract int getLayoutResId();

    @Override
    public void showLoading(boolean cancelable) {
        if (isAttached()) {
            mActivity.showLoading(cancelable);
        }
    }

    @Override
    public void hideLoading() {
        if (isAttached()) {
            mActivity.hideLoading();
        }
    }
}
