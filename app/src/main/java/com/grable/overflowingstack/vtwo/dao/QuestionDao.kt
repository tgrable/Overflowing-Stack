package com.grable.overflowingstack.vtwo.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import com.grable.overflowingstack.vtwo.models.QuestionRaw

/**
 * Created by timgrable on 2/16/18.
 */
@Dao
interface QuestionDao {
    @Query("SELECT * FROM question")
    fun getAll(): List<QuestionRaw>

    @Insert(onConflict = REPLACE)
    fun insertQuestion(questionRaw: QuestionRaw)
}