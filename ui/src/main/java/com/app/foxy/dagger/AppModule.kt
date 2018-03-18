package com.app.foxy.dagger

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Module of the application using dagger 2.
 */
@Module
class AppModule(private val application: Application) {

    @Singleton
    @Provides
    fun provideContext(): Context = application

    @Provides
    @Singleton
    fun provideApplication(): Application = application
}
