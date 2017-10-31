package com.lx.potato.common.util;

import android.util.Log;

import com.lx.potato.model.entity.BaseResponse;
import com.lx.potato.model.remote.ApiException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lixiang on 2017/10/29.
 */
public final class RetrofitUtil {
//    Observable
//            .map                    // 操作1
//            .flatMap                // 操作2
//            .subscribeOn(io)
//            .map                    //操作3
//            .flatMap                //操作4
//            .observeOn(main)
//            .map                    //操作5
//            .flatMap                //操作6
//            .subscribeOn(io)        //!!特别注意
//            .subscribe(handleData)
//    操作1，操作2是在io线程上，因为之后subscribeOn切换了线程
//    操作3，操作4也是在io线程上，因为在subscribeOn切换了线程之后，并没有发生改变。
//    操作5，操作6是在main线程上，因为在他们之前的observeOn切换了线程。
//    特别注意那一段，对于操作5和操作6是无效的
    public static <T> ObservableTransformer<BaseResponse<T>, T> applySchedulersAndFlatResponse() {
        ObservableTransformer<BaseResponse<T>, T> transformer = new ObservableTransformer<BaseResponse<T>, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<BaseResponse<T>> upstream) {
                return upstream
                        .subscribeOn(Schedulers.io()) // 指定产生事件的线程，设置过一次后就不会改变
                        .flatMap(new Function<BaseResponse<T>, ObservableSource<T>>() {
                            @Override
                            public ObservableSource<T> apply(@NonNull BaseResponse<T> response) throws Exception {
                                return flatResponseInternal(response);
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread()); // 指定消费事件的线程，每调用一次observeOn都会切换下个操作执行的线程
            }
        };
        return transformer;
    }

    public static <T> ObservableTransformer<BaseResponse<T>, T> flatResponse() {
        ObservableTransformer<BaseResponse<T>, T> transformer = new ObservableTransformer<BaseResponse<T>, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<BaseResponse<T>> upstream) {
                return upstream.flatMap(new Function<BaseResponse<T>, ObservableSource<T>>() {
                            @Override
                            public ObservableSource<T> apply(@NonNull BaseResponse<T> response) throws Exception {
                                return flatResponseInternal(response);
                            }
                        });
            }
        };
        return transformer;
    }

    private static <T> Observable<T> flatResponseInternal(final BaseResponse<T> response) {
        Observable<T> observable = Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<T> e) throws Exception {
                Log.e("lx", "flatResponse: " + Thread.currentThread().getName());
                if (!response.isError()) {
                    if (!e.isDisposed()) {
                        e.onNext(response.getResults());
                    }
                } else {
                    if (!e.isDisposed()) {
                        e.onError(new ApiException(response.getErrorCode(), response.getErrorMsg()));
                    }
                    return;
                }
                if (!e.isDisposed()) {
                    e.onComplete();
                }
            }
        });
        return observable;
    }
}
