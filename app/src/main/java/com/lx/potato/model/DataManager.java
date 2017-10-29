package com.lx.potato.model;

import android.util.Log;

import com.lx.potato.model.entity.Gank;
import com.lx.potato.model.local.SharedPrefHelper;
import com.lx.potato.model.remote.ApiService;
import com.lx.potato.model.remote.ApiServiceCreator;
import com.lx.potato.util.RetrofitUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lixiang on 2017/10/28.<br/>
 */
public final class DataManager {
    private static DataManager instance;
    private ApiService mApiService;
    private SharedPrefHelper mSharedPrefHelper;

    private DataManager() {
        mApiService = ApiServiceCreator.create();
        mSharedPrefHelper = new SharedPrefHelper();
    }

    public static DataManager getInstance() {
        if (instance == null) {
            synchronized (DataManager.class) {
                if (instance == null) {
                    instance = new DataManager();
                }
            }
        }
        return instance;
    }

    public Observable<List<Gank>> getGank() {
        return mApiService.getGank()
                .compose(RetrofitUtil.<List<Gank>>applySchedulersAndFlatResponse());
                // 要缓存请求结果的添加以下代码
//                .observeOn(Schedulers.io())
//                .doOnNext(new Consumer<List<Gank>>() {
//                    @Override
//                    public void accept(List<Gank> ganks) throws Exception {
//                        // 写入缓存
//                        Log.e("lx", "doOnNext---: " + Thread.currentThread().getName());
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<List<Gank>> getGank2FromDisk() {
        return Observable.create(new ObservableOnSubscribe<List<Gank>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Gank>> e) throws Exception {
                Log.e("lx", "getGank2FromDisk: " + Thread.currentThread().getName());
                List<Gank> list = new ArrayList<>();
                Gank gank = new Gank();
                gank.desc = "from disk";
                list.add(gank);
                if (!e.isDisposed()) {
                    e.onNext(list);
                    e.onComplete();
                }
            }
        });
    }

    /**
     * 先从缓存再从网络读取数据
     *
     * @return
     */
    public Observable<List<Gank>> getGankFromDiskAndNet() {
        Observable<List<Gank>> net = getGank();
        return Observable.concat(getGank2FromDisk(), net)
                .subscribeOn(Schedulers.io());
//                .observeOn(AndroidSchedulers.mainThread());
    }
}
