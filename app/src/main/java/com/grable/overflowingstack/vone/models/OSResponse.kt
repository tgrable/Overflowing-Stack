package com.grable.overflowingstack.vone.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.grable.overflowingstack.vtwo.models.Question

/**
 * Created by timgrable on 10/31/17.
 */

class OSResponse {
    @SerializedName("items")
    @Expose
    var items: List<Question> = arrayListOf()

    @SerializedName("quota_remaining")
    @Expose
    var quotaRemaining: Int? = null
}
