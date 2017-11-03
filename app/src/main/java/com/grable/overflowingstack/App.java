package com.grable.overflowingstack;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.grable.overflowingstack.interfaces.RequestInterface;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.Map;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by timgrable on 10/30/17.
 */

public class App extends Application {
    public static final String TAG = "tgrable";
    public static final String BASE_URL = "https://api.stackexchange.com/";
    public static final String CACHE = "max-age=3600, max-stale=259200";

    public static final String QUESTION_DB = "question";
    public static final String ANSWER_DB = "answer";


    public static OkHttpClient okHttpClient;
    public static Retrofit retrofit;

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        this.context = getApplicationContext();

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

    /**
     * Utility function to set the margins for answer objects
     *
     */
    public static void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    /**
     * Utility function to set the Shared Preferences for user score
     *
     */
    public static void setUserScore(int score) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("user_score", score);
        editor.apply();
    }

    /**
     * Utility function to get the Shared Preferences for user score
     *
     */
    public static int getUserScore() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int score = preferences.getInt("user_score", 0);

        return score;
    }

    /**
     * Utility function to log all the Shared Preferences
     *
     */
    public static void logAllCurrentPreferences() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Map<String,?> keys = preferences.getAll();

        for(Map.Entry<String,?> entry : keys.entrySet()){
            Log.d(App.TAG, entry.getKey() + ": " + entry.getValue().toString());
        }

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        Log.d(App.TAG, metrics.toString());
    }
}
