package com.lx.potato.ui.widget.refresh;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by lixiang on 2017/11/5.
 */
public class PtrLayout extends FrameLayout {
    public interface OnRefreshListener {
        void onRefreshChange(float factor);

        void onRefreshStart();
    }

    private static final int STATE_RELEASE_TO_REFRESH = 1;
    private static final int STATE_PULL_TO_REFRESH = 2;
    private static final int STATE_REFRESHING = 3;
    private static final int STATE_REFRESH_FINISHED = 4;
    private View mHeaderView, mContentView, mFooterView;
    private boolean isHeaderLeftStartPosition, isFooterLeftStartPosition;
    private float mLastY;
    private ValueAnimator animator;
    private int mCurState = STATE_REFRESH_FINISHED;
    private OnRefreshListener mOnRefreshListener = new OnRefreshListener() {
        @Override
        public void onRefreshChange(float factor) {
//            logMsg(factor);
        }

        @Override
        public void onRefreshStart() {
            logMsg("onRefreshStart");
        }
    };

    private void logMsg(Object o) {
        Log.e("lx", o.toString());
    }

    public PtrLayout(@NonNull Context context) {
        this(context, null);
    }

    public PtrLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PtrLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TextView header = new TextView(context);
        header.setTextSize(TypedValue.COMPLEX_UNIT_SP, 27);
        header.setTextColor(0xff00ff00);
        header.setBackgroundColor(0xff0000ff);
        header.setText("Header");
        header.setGravity(Gravity.CENTER);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 240);
        addView(header, lp);
        mHeaderView = header;
        animator = ValueAnimator.ofInt(0, 0);
        animator.setDuration(300);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int scrollY = (int) valueAnimator.getAnimatedValue();
                setScrollY(scrollY);
                if (mOnRefreshListener != null) {
                    float factor = scrollY * 1f / (-mHeaderView.getMeasuredHeight());
                    mOnRefreshListener.onRefreshChange(factor);
                }
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isHeaderLeftStartPosition = false;
                isFooterLeftStartPosition = false;
                mCurState = STATE_REFRESH_FINISHED;
            }
        });
    }

    @Override
    protected void onFinishInflate() {
        mContentView = getChildAt(1);
        TextView header = new TextView(getContext());
        header.setTextSize(TypedValue.COMPLEX_UNIT_SP, 27);
        header.setTextColor(0xff00ff00);
        header.setBackgroundColor(0xff0000ff);
        header.setText("Footer");
        header.setGravity(Gravity.CENTER);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 180);
        addView(header, lp);
        mFooterView = header;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            mHeaderView.layout(0, -mHeaderView.getMeasuredHeight(), mHeaderView.getMeasuredWidth(), 0);
            mContentView.layout(0, 0, mContentView.getMeasuredWidth(), mContentView.getMeasuredHeight());
            mFooterView.layout(0, mContentView.getMeasuredHeight(),
                    mFooterView.getMeasuredWidth(), mContentView.getMeasuredHeight() + mFooterView.getMeasuredHeight());
        }
    }

    private boolean dispatchTouchEventSuper(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (animator.isRunning()) {
            return true;
        }
        float curY = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = curY;
                break;
            case MotionEvent.ACTION_MOVE:
                boolean canScrollUp = canScrollUp();
//                logMsg("canScrollUp:" + canScrollUp);
                int offsetY = (int) (curY - mLastY);
                mLastY = curY;
                /*处理下拉刷新*/
                // scrollY大于0时，header已经不可见
                int scrollY = getScrollY();
                if (scrollY > 0 && isHeaderLeftStartPosition) {
                    clearContentViewFocus(true);
                    scrollTo(0, 0);
                    isHeaderLeftStartPosition = false;
                    return dispatchTouchEventSuper(ev);
                }
                if (!canScrollUp && scrollY == 0 && offsetY > 0 || isHeaderLeftStartPosition) {
                    clearContentViewFocus(true);
                    isHeaderLeftStartPosition = true;
                    scrollBy(0, (int) (-offsetY / 1.7f));
                    if (mOnRefreshListener != null) {
                        float factor = getScrollY() * 1f / (-mHeaderView.getMeasuredHeight());
                        mOnRefreshListener.onRefreshChange(factor);
                    }
                    return true;
                }
                /*处理上拉加载*/
                if (scrollY < 0 && isFooterLeftStartPosition) {
                    clearContentViewFocus(true);
                    scrollTo(0, 0);
                    isHeaderLeftStartPosition = false;
                    return dispatchTouchEventSuper(ev);
                }
                boolean canScrollDown = canScrollDown();
                if (!canScrollDown && scrollY == 0 && offsetY < 0 || isFooterLeftStartPosition) {
                    clearContentViewFocus(true);
                    isFooterLeftStartPosition = true;
                    scrollBy(0, (int) (-offsetY / 1.7f));
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mCurState == STATE_REFRESHING) {
                    restoreHeader();
                    break;
                }
                // header已全部显示出来，进入刷新状态
                if (getScrollY() <= -mHeaderView.getMeasuredHeight()) {
                    mCurState = STATE_REFRESHING;
                    if (mOnRefreshListener != null) {
                        mOnRefreshListener.onRefreshStart();
                    }
                } else if (getScrollY() >= mFooterView.getMeasuredHeight()) {
                    mCurState = STATE_REFRESHING;
                    if (mOnRefreshListener != null) {
                        mOnRefreshListener.onRefreshStart();
                    }
                } else {
                    clearContentViewFocus(false);
                    restoreHeader();
                }
                break;
        }
        return dispatchTouchEventSuper(ev);
    }

    /**
     * 防止在下拉或上拉的过程中误触发content view的点击事件
     *
     * @param clear
     */
    private void clearContentViewFocus(boolean clear) {
        mContentView.setPressed(!clear);
        mContentView.setFocusable(!clear);
        mContentView.setFocusableInTouchMode(!clear);
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y < -mHeaderView.getMeasuredHeight()) {
            y = -mHeaderView.getMeasuredHeight();
        } else if (y > mFooterView.getMeasuredHeight()) {
            y = mFooterView.getMeasuredHeight();
        }
        super.scrollTo(x, y);
    }

    /**
     * 下拉刷新用
     *
     * @return
     */
    private boolean canScrollUp() {
        return ViewCompat.canScrollVertically(mContentView, -1);
    }

    /**
     * 上拉加载用
     *
     * @return
     */
    private boolean canScrollDown() {
        return ViewCompat.canScrollVertically(mContentView, 1);
    }

    private void restoreHeader() {
        if (getScrollY() == 0) {
            return;
        }
        animator.setIntValues(getScrollY(), 0);
        animator.start();
    }

    public void endRefresh() {
        restoreHeader();
    }
}
