package com.grable.overflowingstack.vtwo.dagger.module

import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import android.arch.persistence.room.Room
import com.grable.overflowingstack.vtwo.application.App
import com.grable.overflowingstack.vtwo.dao.QuestionDao
import com.grable.overflowingstack.vtwo.database.AppDatabase


/**
 * Created by timgrable on 2/15/18.
 */
@Module
class RoomModule {

    @Singleton
    @Provides
    fun providesRoomDatabase(app: App): AppDatabase {
        return Room.databaseBuilder(app, AppDatabase::class.java, "response-item").build()
    }
}