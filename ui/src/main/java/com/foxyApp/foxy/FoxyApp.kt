package com.foxyApp.foxy

import android.content.Context
import android.os.StrictMode
import android.support.multidex.MultiDexApplication
import com.foxyApp.domain.Domain
import com.foxyApp.foxy.dagger.AppComponent
import com.foxyApp.foxy.dagger.AppModule
import com.foxyApp.foxy.dagger.DaggerAppComponent

class FoxyApp : MultiDexApplication() {

    private var mAppComponent: AppComponent? = null

    override fun onCreate() {
        super.onCreate()
        // Used to detect FileUriExposedException
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        builder.detectFileUriExposure()
        // App component initialization for dagger
        mAppComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
        mAppComponent?.inject(this)
        // Initialize the context of the domain layer.
        Domain.init(this)
    }

    /**
     * @return the dependency injection application component.
     */
    fun getAppComponent(): AppComponent? = mAppComponent

    companion object {

        /**
         * Returns this custom application.
         * @return this
         */
        fun get(context: Context): FoxyApp = context.applicationContext as FoxyApp
    }
}
