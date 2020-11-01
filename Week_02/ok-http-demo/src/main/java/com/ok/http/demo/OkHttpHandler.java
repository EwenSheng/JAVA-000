package com.ok.http.demo;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * @author: Ewen
 * @program: ok-http-demo
 * @date: 2020/10/27 22:09
 * @description:
 */
public class OkHttpHandler {

    public static final String REQUEST_URL = "Http://localhost:8801";

    public static void main(String[] args) throws IOException {

        System.out.println(get(REQUEST_URL));
    }

    private static String get(String url) throws IOException {

        Request request = new Request
                .Builder()
                .url(url)
                .build();

        Response response = new OkHttpClient()
                .newCall(request)
                .execute();

        return response.body().string();
    }
}
