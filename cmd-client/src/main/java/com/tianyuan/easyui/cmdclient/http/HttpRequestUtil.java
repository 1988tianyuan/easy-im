package com.tianyuan.easyui.cmdclient.http;

import com.google.common.net.HttpHeaders;
import com.tianyuan.easyim.common.util.JsonUtil;
import com.tianyuan.easyui.cmdclient.exception.HttpRequestException;
import io.netty.handler.codec.http.HttpMethod;
import okhttp3.*;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

public class HttpRequestUtil {

    public static String stringGetRequest(String url) {
        return httpExec(url, HttpMethod.GET, null, null);
    }

    public static String jsonPostRequest(String url, Object body) {
        String json = JsonUtil.toJson(body);
        Headers headers = new Headers.Builder()
                .add(HttpHeaders.CONTENT_TYPE, "application/json").build();
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json"), json);
        return httpExec(url, HttpMethod.POST, headers, requestBody);
    }

    public static String httpExec(String url, HttpMethod method, Headers headers, RequestBody requestBody) {
        try {
            Request.Builder builder = new Request.Builder()
                    .method(method.name(), requestBody)
                    .url(url);
            if (headers != null) {
                builder.headers(headers);
            }
            Call call = SingletonHolder.instance.newCall(builder.build());
            Response response = call.execute();
            if (response.isSuccessful()) {
                ResponseBody responseBody = response.body();
                return responseBody != null ? responseBody.string() : null;
            } else {
                throw new HttpRequestException(MessageFormat.format("Failed to get response from {0}, http code: {1}, response: {2}",
                        url, response.code(), response.body() != null ? response.body().string() : ""));
            }
        } catch (Exception e) {
            throw new HttpRequestException(MessageFormat.format("Failed to handle http response from {0}, reason: {1}",
                    url, e.getMessage()), e);
        }
    }

    private static class SingletonHolder {
        private static OkHttpClient instance = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS).build();
    }
}
