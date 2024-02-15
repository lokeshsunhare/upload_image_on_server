package com.goldentechinfo.insectimageupload;

import android.content.Context;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeaderInterceptor implements Interceptor {

    private Context context = null;

    public HeaderInterceptor(Context context) {
        this.context = context;
    }

    private static final String TAG = "HeaderInterceptor";

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request()
                .newBuilder()
                .addHeader("app-version", BuildConfig.VERSION_NAME) //insert application version here
                .removeHeader("User-Agent")
                .addHeader("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:38.0) Gecko/20100101 Firefox/38.0")
                .build();
        Response response = chain.proceed(request);

        return response;

    }
}