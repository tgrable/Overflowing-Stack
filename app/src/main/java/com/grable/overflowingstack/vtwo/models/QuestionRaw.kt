package com.grable.overflowingstack.vtwo.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.grable.overflowingstack.vone.models.Answer

/**
 * Created by timgrable on 2/16/18.
 */
@Entity(tableName = "question")
class QuestionRaw {

    @PrimaryKey
    @SerializedName("question_id")
    @Expose
    var question_id: Long = 0

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("body")
    @Expose
    var body: String? = null

    @SerializedName("body_markdown")
    @Expose
    var body_markdown: String? = null

    @SerializedName("creation_date")
    @Expose
    var creation_date: String? = null

    @SerializedName("isGuessed")
    @Expose
    var isGuessed: Boolean? = null

//    @SerializedName("answers")
//    @Expose
//    private var answers: List<Answer>? = null
}