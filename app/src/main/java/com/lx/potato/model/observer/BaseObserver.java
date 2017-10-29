package com.lx.potato.model.observer;

import com.google.gson.stream.MalformedJsonException;
import com.lx.potato.model.remote.ApiException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;

/**
 * Created by lixiang on 2017/10/29.
 */
public abstract class BaseObserver<T> implements Observer<T> {
    private Disposable mDisposable;

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        mDisposable = d;
    }

    @Override
    public void onNext(@NonNull T response) {
        onSuccess(response);
    }

    @Override
    public void onError(@NonNull Throwable e) {
        ApiException errorInfo;
        if (e instanceof CompositeException) {
            errorInfo = new ApiException();
            CompositeException compositeE = (CompositeException) e;
            for (Throwable throwable : compositeE.getExceptions()) {
                if (throwable instanceof SocketTimeoutException) {
                    errorInfo.setErrorCode(ApiException.CODE_TIME_OUT);
                    errorInfo.setErrorMsg(throwable.getMessage());
                } else if (throwable instanceof ConnectException) {
                    errorInfo.setErrorCode(ApiException.CODE_UNCONNECTED);
                    errorInfo.setErrorMsg(throwable.getMessage());
                } else if (throwable instanceof UnknownHostException) {
                    errorInfo.setErrorCode(ApiException.CODE_UNCONNECTED);
                    errorInfo.setErrorMsg(throwable.getMessage());
                } else if (throwable instanceof MalformedJsonException) {
                    errorInfo.setErrorCode(ApiException.CODE_JSON_ERROR);
                    errorInfo.setErrorMsg(throwable.getMessage());
                }
            }
        } else if (e instanceof ApiException) {
            errorInfo = (ApiException) e;
        } else {
            errorInfo = new ApiException();
        }
        onFail(errorInfo);
    }

    @Override
    public void onComplete() {
    }

    public abstract void onSuccess(@NonNull T data);

    public abstract void onFail(@NonNull ApiException error);

    public void dispose() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    private static BaseObserver EMPTY = new BaseObserver() {
        @Override
        public void onSuccess(@NonNull Object data) {
        }

        @Override
        public void onFail(@NonNull ApiException error) {
        }
    };

    public static BaseObserver emptyObserver() {
        return EMPTY;
    }
}
