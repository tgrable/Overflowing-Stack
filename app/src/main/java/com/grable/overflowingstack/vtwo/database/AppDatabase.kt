package com.grable.overflowingstack.vtwo.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.grable.overflowingstack.vtwo.dao.QuestionDao
import com.grable.overflowingstack.vtwo.models.QuestionRaw

/**
 * Created by timgrable on 2/15/18.
 */
@Database(entities = [(QuestionRaw::class)], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun questionItemDao(): QuestionDao
}