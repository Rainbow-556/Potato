package com.lx.potato.model.remote;

import android.support.annotation.NonNull;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.lx.potato.common.util.LogUtil;
import com.lx.potato.model.remote.interceptor.HeaderAndSignInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lixiang on 2017/10/28.
 */
public final class ApiServiceCreator {
    private static final String BASE_URL = "http://www.qq.com";

    private ApiServiceCreator() {
    }

    public static ApiService create() {
        HttpLoggingInterceptor loggingInterceptor = getLoggingInterceptor();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(new HeaderAndSignInterceptor());
        OkHttpClient okHttpClient = builder.build();
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .build();
        return retrofit.create(ApiService.class);
    }

    @NonNull
    private static HttpLoggingInterceptor getLoggingInterceptor() {
        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                LogUtil.logApiRequest(message);
            }
        });
        loggingInterceptor.setLevel(level);
        return loggingInterceptor;
    }
}
