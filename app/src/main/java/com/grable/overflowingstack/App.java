package com.grable.overflowingstack;

import android.app.Application;

import com.grable.overflowingstack.interfaces.RequestInterface;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by timgrable on 10/30/17.
 */

public class App extends Application {
    public static final String TAG = "tgrable";
    public static final String BASE_URL = "http://5adbffc0980c238501d4-6ebc8dce445dc8adb8d88970e09e0fb4.r0.cf2.rackcdn.com/";
    public static final String CACHE = "max-age=3600, max-stale=259200";

    public static OkHttpClient okHttpClient;
    public static Retrofit retrofit;

    @Override
    public void onCreate() {
        super.onCreate();

        int cacheSize = 10 * 1024 * 1024; // 10 MB
        Cache cache = new Cache(this.getCacheDir(), cacheSize);

        okHttpClient = new OkHttpClient.Builder()
                .cache(cache)
                .build();

        GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
}
