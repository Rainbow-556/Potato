package com.lx.potato.model.remote.interceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by lixiang on 2017/10/31.
 */
public class HeaderAndSignInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request oldRequest = chain.request();
        Request.Builder newRequestBuilder = oldRequest.newBuilder();
        // generate sign
        String sign = "sign";
        final String method = oldRequest.method();
        if ("POST".equalsIgnoreCase(method)) {
            Map<String, String> oldQueryParams = parseParams(oldRequest);
            if (oldQueryParams != null) {
                FormBody.Builder bodyBuilder = new FormBody.Builder();
                Iterator<Map.Entry<String, String>> entryIterator = oldQueryParams.entrySet().iterator();
                while (entryIterator.hasNext()) {
                    Map.Entry<String, String> entry = entryIterator.next();
                    String key = entry.getKey();
                    String value = entry.getValue();
                    bodyBuilder.add(key, value);
                }
                bodyBuilder.add("Sign", sign);
                newRequestBuilder.post(bodyBuilder.build());
            }
        } else if ("GET".equalsIgnoreCase(method)) {
            HttpUrl.Builder httpUrlBuilder = oldRequest.url().newBuilder();
            httpUrlBuilder.addQueryParameter("sign", sign);
            newRequestBuilder.url(httpUrlBuilder.build());
        }
//        if ("GET".equalsIgnoreCase(oldRequest.method())) {
//            HttpUrl.Builder httpUrlBuilder = oldRequest.url().newBuilder();
//            for (String key : params.keySet()) {
//                httpUrlBuilder.addQueryParameter(key, params.get(key));
//            }
//            newRequestBuilder.url(httpUrlBuilder.build());
//        } else {
//            if (oldRequest.body() instanceof FormBody) {
//                FormBody.Builder formBodyBuilder = new FormBody.Builder();
//                for (String key : params.keySet()) {
//                    formBodyBuilder.add(key, params.get(key));
//                }
//                FormBody oldFormBody = (FormBody) oldRequest.body();
//                int size = oldFormBody.size();
//                for (int i = 0; i < size; i++) {
//                    formBodyBuilder.add(oldFormBody.name(i), oldFormBody.value(i));
//                }
//                newRequestBuilder.post(formBodyBuilder.build());
//            }
//        }
        // add header
        newRequestBuilder.addHeader("Token", "token");
        newRequestBuilder.addHeader("HaHa", "token");
        return chain.proceed(newRequestBuilder.build());
    }

    /**
     * 解析请求参数
     *
     * @param request
     * @return
     */
    private static Map<String, String> parseParams(Request request) {
        //GET POST DELETE PUT PATCH
        String method = request.method();
        Map<String, String> params = null;
        if ("GET".equals(method)) {
            params = doGet(request);
        } else if ("POST".equals(method) || "PUT".equals(method) || "DELETE".equals(method) || "PATCH".equals(method)) {
            RequestBody body = request.body();
            if (body != null && body instanceof FormBody) {
                params = doForm(request);
            }
        }
        return params;
    }

    /**
     * 获取get方式的请求参数
     *
     * @param request
     * @return
     */
    private static Map<String, String> doGet(Request request) {
        Map<String, String> params = null;
        HttpUrl url = request.url();
        Set<String> strings = url.queryParameterNames();
        if (strings != null) {
            Iterator<String> iterator = strings.iterator();
            params = new HashMap<>();
            int i = 0;
            while (iterator.hasNext()) {
                String name = iterator.next();
                String value = url.queryParameterValue(i);
                params.put(name, value);
                i++;
            }
        }
        return params;
    }

    /**
     * 获取表单的请求参数
     *
     * @param request
     * @return
     */
    private static Map<String, String> doForm(Request request) {
        Map<String, String> params = null;
        FormBody body = null;
        try {
            body = (FormBody) request.body();
        } catch (ClassCastException c) {
        }
        if (body != null) {
            int size = body.size();
            if (size > 0) {
                params = new HashMap<>();
                for (int i = 0; i < size; i++) {
                    params.put(body.name(i), body.value(i));
                }
            }
        }
        return params;
    }
}