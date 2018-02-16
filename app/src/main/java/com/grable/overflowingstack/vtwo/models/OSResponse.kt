package com.grable.overflowingstack.vtwo.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by timgrable on 2/16/18.
 */
class OSResponse {
    @SerializedName("items")
    @Expose
    var items: List<QuestionRaw> = arrayListOf()

    @SerializedName("quota_remaining")
    @Expose
    var quotaRemaining: Int? = null
}