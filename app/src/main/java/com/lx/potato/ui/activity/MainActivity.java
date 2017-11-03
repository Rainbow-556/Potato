package com.lx.potato.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.lx.potato.R;
import com.lx.potato.model.DataManager;
import com.lx.potato.model.entity.Gank;
import com.lx.potato.model.observer.BaseObserver;
import com.lx.potato.model.remote.ApiException;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv);
    }

    private void getGank_4() {
        DataManager.getInstance().getGank().subscribe(BaseObserver.emptyObserver());
    }

    private void getGank_3() {
        DataManager.getInstance().getGank().subscribe(new BaseObserver<List<Gank>>() {
            @Override
            public void onSuccess(@NonNull List<Gank> data) {
                Log.e("lx", "onSuccess: " + data.size());
            }

            @Override
            public void onFail(@NonNull ApiException error) {
                Log.e("lx", "onFail: " + error.toString());
            }
        });
    }

    private void getGank() {
        DataManager.getInstance().getGank().subscribe(new Observer<List<Gank>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.e("lx", "onSubscribe: " + Thread.currentThread().getName());
            }

            @Override
            public void onNext(@NonNull List<Gank> ganks) {
                Log.e("lx", "onNext: " + Thread.currentThread().getName());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("lx", "onError: " + Thread.currentThread().getName());
            }

            @Override
            public void onComplete() {
                Log.e("lx", "onComplete: " + Thread.currentThread().getName());
            }
        });
    }

    private void getGank_2() {
        DataManager.getInstance().getGankFromDiskAndNet().subscribe(new Observer<List<Gank>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.e("lx", "onSubscribe");
            }

            @Override
            public void onNext(@NonNull List<Gank> ganks) {
                Log.e("lx", "onNext, size: " + ganks.size());
                tv.append(", " + ganks.size());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("lx", "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.e("lx", "onComplete:");
            }
        });
    }
}
