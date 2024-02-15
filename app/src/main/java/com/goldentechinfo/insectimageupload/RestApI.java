package com.goldentechinfo.insectimageupload;


import android.annotation.SuppressLint;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestApI {

    @SuppressLint("StaticFieldLeak")
    private static RestApI mInstance;
    private final Context context;

//    private static final String BASE_URL = "http://192.168.101.9:8000/api/";

    // API_FOR 1 for Chips 2 for nic
    public RestApI(Context context) {
        this.context = context;
    }

    private final static ReentrantLock lock = new ReentrantLock();

    public static RestApI getInstance(Context context) {
        // Critical section of code
        // Only one thread can execute this block at a time
        if (mInstance == null) {
            lock.lock();
            try {
                synchronized (RestApI.class) {
                    if (mInstance == null) {
                        mInstance = new RestApI(context);
                    }
                }
            } finally {
                lock.unlock();
            }
        }
        return mInstance;
    }

    private static Retrofit retrofit;

    public InsectApi SetRetrofit() {

        OkHttpClient okHttpClient;
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.MINUTES) // connect timeout
                .readTimeout(30, TimeUnit.MINUTES) // read timeout
                .writeTimeout(15, TimeUnit.MINUTES)
                .addInterceptor(new HeaderInterceptor(context))
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();

        return retrofit.create(InsectApi.class);
    }


}
