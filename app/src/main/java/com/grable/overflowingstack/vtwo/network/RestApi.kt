package com.grable.overflowingstack.vtwo.network

import com.grable.overflowingstack.vtwo.models.OSResponse
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET

/**
 * Created by timgrable on 2/13/18.
 */
interface RestApi {
    @GET("2.2/search/advanced?order=desc&sort=creation&accepted=True&answers=2&tagged=Android&site=stackoverflow&filter=!3yXvh9VWMxAG3jEhT")
    fun getQuestions(): Observable<OSResponse>

    @GET("2.2/search/advanced?order=desc&sort=creation&accepted=True&answers=2&tagged=Android&site=stackoverflow&filter=!3yXvh9VWMxAG3jEhT")
    fun getResponse(): Single<OSResponse>
}