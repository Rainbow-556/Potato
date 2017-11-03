package com.lx.potato.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;

import com.lx.potato.R;

/**
 * Created by lixiang on 2017/11/3.
 */
public final class FButton extends android.support.v7.widget.AppCompatTextView {
    private static final int BTN_SIZE_SMALL = 1;
    private static final int BTN_SIZE_MIDDLE = 2;
    private static final int BTN_SIZE_LARGER = 3;
    private static final int BTN_SIZE_SMS_CODE = 4;
    private int mBtnSize;
    private int mBtnColor;

    public FButton(Context context) {
        this(context, null);
    }

    public FButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setClickable(true);
        setGravity(Gravity.CENTER);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FButton);
        mBtnSize = array.getInt(R.styleable.FButton_btn_size, BTN_SIZE_SMALL);
        mBtnColor = array.getInt(R.styleable.FButton_btn_color, 0);
        if (mBtnColor == 1) {
            setBackgroundResource(R.drawable.selector_btn_middle_round_yellow);
        }
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = 0, h = 0;
        switch (mBtnSize) {
            case BTN_SIZE_SMALL:
                w = 300;
                h = 85;
                break;
            case BTN_SIZE_MIDDLE:
                w = 600;
                h = 150;
                break;
            case BTN_SIZE_LARGER:
                w = 1200;
                h = 300;
                break;
            case BTN_SIZE_SMS_CODE:
                w = 100;
                h = 70;
                break;
        }
        setMeasuredDimension(w, h);
    }
}
