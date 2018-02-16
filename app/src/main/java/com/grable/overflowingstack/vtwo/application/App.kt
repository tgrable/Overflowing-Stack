package com.grable.overflowingstack.vtwo.application

import android.app.Application
import com.grable.overflowingstack.vtwo.dagger.component.DaggerNetworkComponent
import com.grable.overflowingstack.vtwo.dagger.component.NetworkComponent
import com.grable.overflowingstack.vtwo.dagger.module.AppModule
import com.grable.overflowingstack.vtwo.dagger.module.NetworkModule

/**
 * Created by timgrable on 2/13/18.
 */
class App : Application() {

    private var netComponent: NetworkComponent? = null

    override fun onCreate() {
        super.onCreate()

        netComponent = DaggerNetworkComponent.builder()
                .appModule(AppModule(this))
                .networkModule(NetworkModule())
                .build()
    }

    fun getNetworkComponent(): NetworkComponent? {
        return netComponent
    }
}