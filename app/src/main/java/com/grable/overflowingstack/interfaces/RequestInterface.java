package com.grable.overflowingstack.interfaces;

import com.grable.overflowingstack.App;
import com.grable.overflowingstack.models.OSResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;

/**
 * Created by timgrable on 10/30/17.
 */

public interface RequestInterface {
    @GET("android/questions_v1.json")
    @Headers("Cache-Control: " + App.CACHE)
    Observable<OSResponse> getQuestions();
}
