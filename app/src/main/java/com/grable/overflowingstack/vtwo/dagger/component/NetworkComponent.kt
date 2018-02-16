package com.grable.overflowingstack.vtwo.dagger.component

import com.grable.overflowingstack.vtwo.dagger.module.AppModule
import com.grable.overflowingstack.vtwo.dagger.module.NetworkModule
import com.grable.overflowingstack.vtwo.ui.main.MainActivity
import dagger.Component
import javax.inject.Singleton

/**
 * Created by timgrable on 2/13/18.
 */
@Singleton
@Component(modules = [AppModule::class, NetworkModule::class])
interface NetworkComponent {
    fun inject(target: MainActivity)
}