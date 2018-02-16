package com.grable.overflowingstack.vtwo.dagger.module

import android.app.Application
import android.arch.persistence.room.Room
import android.support.v4.view.ViewCompat
import com.grable.overflowingstack.vtwo.database.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by timgrable on 2/13/18.
 */
@Module
class AppModule(private val application: Application) {
    @Provides
    @Singleton
    fun provideApplication(): Application {
        return application
    }

    @Provides
    @Singleton
    fun providesAppDatabase(): AppDatabase = Room.databaseBuilder(application, AppDatabase::class.java, "response-items").build()
}