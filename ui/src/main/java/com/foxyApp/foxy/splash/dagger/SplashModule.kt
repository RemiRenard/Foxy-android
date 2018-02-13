package com.foxyApp.foxy.splash.dagger

import android.content.Context
import dagger.Module
import dagger.Provides
import com.foxyApp.domain.services.user.IUserService
import com.foxyApp.foxy.splash.ISplashPresenter
import com.foxyApp.foxy.splash.SplashPresenter

@Module
class SplashModule {

    @Provides
    @SplashScope
    fun provideSplashPresenter(userService: IUserService, context: Context): ISplashPresenter =
            SplashPresenter(userService, context)
}
