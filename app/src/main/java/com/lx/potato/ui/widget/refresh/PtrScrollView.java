package com.lx.potato.ui.widget.refresh;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.lx.potato.R;

/**
 * Created by lixiang on 2017/11/11.
 */
public final class PtrScrollView extends ScrollView {
    private interface OnRefreshListener {
        void onRefreshChange(float factor);

        void onRefreshStart();
    }

    private static final int STATE_RELEASE_TO_REFRESH = 1;
    private static final int STATE_PULL_TO_REFRESH = 2;
    private static final int STATE_REFRESHING = 3;
    private static final int STATE_REFRESH_FINISHED = 4;
    private int mCurState = STATE_REFRESH_FINISHED;
    private LinearLayout.LayoutParams lpHeader;
    private LinearLayout.LayoutParams lpAnimView;
    private View mHeaderView, mAnimView;
    private int mAnimViewMaxBottomMargin = 50;
    private LinearLayout mContentView;
    private float mLastTouchY;
    private ValueAnimator mHeaderTopMarginAnim, mImgBottomMarginAnim;
    private OnRefreshListener mOnRefreshListener = new OnRefreshListener() {
        @Override
        public void onRefreshChange(float factor) {
//            logMsg(factor);
        }

        @Override
        public void onRefreshStart() {
            msg("onRefreshStart");
        }
    };

    public PtrScrollView(Context context) {
        this(context, null);
    }

    public PtrScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PtrScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAnim();
    }

    @Override
    protected void onFinishInflate() {
        mContentView = (LinearLayout) getChildAt(0);
        View header = LayoutInflater.from(getContext()).inflate(R.layout.refresh_header, mContentView, false);
        mContentView.addView(header, 0);
        mHeaderView = header;
        mAnimView = mHeaderView.findViewById(R.id.iv_anim);
    }

    private boolean dispatchTouchEventSuper(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mImgBottomMarginAnim.isRunning() || mHeaderTopMarginAnim.isRunning()) {
            return true;
        }
        if (mCurState == STATE_REFRESHING) {
            return dispatchTouchEventSuper(ev);
        }
        float curY = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastTouchY = curY;
                break;
            case MotionEvent.ACTION_MOVE:
                int offsetY = (int) (curY - mLastTouchY);
                mLastTouchY = curY;
                /*处理下拉刷新*/
                boolean atTop = !canScrollUp();
                if (offsetY < 0) {
                    int dy = (int) (offsetY / 1.7f);
                    if (lpAnimView != null) {
                        lpAnimView = (LinearLayout.LayoutParams) mAnimView.getLayoutParams();
                        if (lpAnimView.bottomMargin > 0) {
                            mCurState = STATE_PULL_TO_REFRESH;
                            clearContentViewFocus(true);
                            int animViewBottomMargin = lpAnimView.bottomMargin + dy;
                            if (animViewBottomMargin <= 0) {
                                lpAnimView.bottomMargin = 0;
                            }
                            lpAnimView.bottomMargin = animViewBottomMargin;
                            mAnimView.setLayoutParams(lpAnimView);
                            return true;
                        }
                    }
                    if (lpHeader != null) {
                        lpHeader = (LinearLayout.LayoutParams) mHeaderView.getLayoutParams();
                        if (lpHeader.topMargin != (-mHeaderView.getHeight())) {
                            mCurState = STATE_PULL_TO_REFRESH;
                            clearContentViewFocus(true);
                            int headerTopMargin = lpHeader.topMargin + dy;
                            if (headerTopMargin <= (-mHeaderView.getHeight())) {
                                headerTopMargin = -mHeaderView.getHeight();
                            }
                            lpHeader.topMargin = headerTopMargin;
                            mHeaderView.setLayoutParams(lpHeader);
                            return true;
                        }
                    }
                }
                if (atTop && offsetY > 0) {
                    clearContentViewFocus(true);
                    mCurState = STATE_PULL_TO_REFRESH;
                    lpHeader = (LinearLayout.LayoutParams) mHeaderView.getLayoutParams();
                    int dy = (int) (offsetY / 1.7f);
                    int headerTopMargin = lpHeader.topMargin + dy;
                    if (headerTopMargin >= 0) {
                        headerTopMargin = 0;
                        lpAnimView = (LinearLayout.LayoutParams) mAnimView.getLayoutParams();
                        int animViewBottomMargin = lpAnimView.bottomMargin + dy;
                        if (animViewBottomMargin >= mAnimViewMaxBottomMargin) {
                            mCurState = STATE_RELEASE_TO_REFRESH;
                            animViewBottomMargin = mAnimViewMaxBottomMargin;
                        }
                        if (lpAnimView.bottomMargin != animViewBottomMargin) {
                            lpAnimView.bottomMargin = animViewBottomMargin;
                            mAnimView.setLayoutParams(lpAnimView);
                        }
                    }
                    if (lpHeader.topMargin != headerTopMargin) {
                        lpHeader.topMargin = headerTopMargin;
                        mHeaderView.setLayoutParams(lpHeader);
                    }
                    return true;
                } else {
                    clearContentViewFocus(false);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                clearContentViewFocus(false);
                if (mCurState == STATE_RELEASE_TO_REFRESH) {
                    mImgBottomMarginAnim.setIntValues(mAnimViewMaxBottomMargin, 0);
                    mImgBottomMarginAnim.start();
                    break;
                }
                if (mCurState == STATE_PULL_TO_REFRESH) {
                    restoreHeader();
                    break;
                }
                if (mCurState == STATE_REFRESHING) {
                    break;
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

    private void restoreHeader() {
        lpHeader = (LinearLayout.LayoutParams) mHeaderView.getLayoutParams();
        mHeaderTopMarginAnim.setIntValues(lpHeader.topMargin, -mHeaderView.getHeight());
        mHeaderTopMarginAnim.start();
    }

    /**
     * 下拉刷新用
     *
     * @return
     */
    private boolean canScrollUp() {
        return ViewCompat.canScrollVertically(this, -1);
    }

    private void initAnim() {
        mHeaderTopMarginAnim = ValueAnimator.ofInt(0, 0);
        mHeaderTopMarginAnim.setDuration(500);
        mHeaderTopMarginAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                lpHeader = (LinearLayout.LayoutParams) mHeaderView.getLayoutParams();
                lpHeader.topMargin = value;
                mHeaderView.setLayoutParams(lpHeader);
            }
        });
        mHeaderTopMarginAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                lpAnimView = (LinearLayout.LayoutParams) mAnimView.getLayoutParams();
                lpAnimView.bottomMargin = 0;
                mAnimView.setLayoutParams(lpAnimView);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mCurState = STATE_REFRESH_FINISHED;
            }
        });
        //
        mImgBottomMarginAnim = ValueAnimator.ofInt(0, 0);
        mImgBottomMarginAnim.setDuration(500);
        mImgBottomMarginAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                lpAnimView = (LinearLayout.LayoutParams) mAnimView.getLayoutParams();
                lpAnimView.bottomMargin = value;
                mAnimView.setLayoutParams(lpAnimView);
            }
        });
        mImgBottomMarginAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mCurState = STATE_REFRESHING;
                mAnimView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        restoreHeader();
                    }
                }, 1000);
            }
        });
    }

    private void msg(Object o) {
        Log.e("lx", o.toString());
    }
}
