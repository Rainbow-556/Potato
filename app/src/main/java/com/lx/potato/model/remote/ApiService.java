package com.lx.potato.model.remote;

import com.lx.potato.model.entity.BaseResponse;
import com.lx.potato.model.entity.Gank;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;

/**
 * Created by lixiang on 2017/10/28.
 */
public interface ApiService {
    @GET("?key=1")
    Observable<ResponseBody> getQQ();

    @GET("http://gank.io/api/data/Android/10/1?key=key")
    Observable<BaseResponse<List<Gank>>> getGank();
}