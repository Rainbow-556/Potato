package com.lx.potato.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

/**
 * Created by lixiang on 2017/10/12.
 */
public abstract class BaseDialog {
    protected Context mContext;
    protected Dialog mDialog;

    public BaseDialog(Context context) {
        this.mContext = context;
        init();
    }

    protected abstract View getContentView();

    private void init() {
        int fullscreenTheme = android.R.style.Theme_Translucent_NoTitleBar_Fullscreen;
        mDialog = new Dialog(mContext, fullscreenTheme);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setContentView(getContentView());
    }

    public void show() {
        mDialog.show();
    }

    public boolean isShowing() {
        return mDialog.isShowing();
    }

    public void hide() {
        mDialog.dismiss();
    }

    public void dismiss() {
        mDialog.dismiss();
    }

    public void setCancelable(boolean cancelable) {
        mDialog.setCancelable(cancelable);
    }
}
