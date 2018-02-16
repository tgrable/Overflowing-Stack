package com.grable.overflowingstack.vone.interfaces;

import com.grable.overflowingstack.vone.models.OSResponse;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Headers;

/**
 * Created by timgrable on 10/30/17.
 */

public interface RequestInterface {
    @GET("2.2/search/advanced?order=desc&sort=creation&accepted=True&answers=2&tagged=Android&site=stackoverflow&filter=!3yXvh9VWMxAG3jEhT")
//    @Headers("Cache-Control: " + App.CACHE)
    Observable<OSResponse> getQuestions();

    @GET("2.2/search/advanced?order=desc&sort=creation&accepted=True&answers=2&tagged=Android&site=stackoverflow&filter=!3yXvh9VWMxAG3jEhT")
//    @Headers("Cache-Control: " + App.CACHE)
    Single<OSResponse> getResponse();
}
