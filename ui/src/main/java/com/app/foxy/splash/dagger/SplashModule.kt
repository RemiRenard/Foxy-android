package com.app.foxy.splash.dagger

import android.content.Context
import com.app.domain.services.global.IGlobalService
import com.app.domain.services.user.IUserService
import com.app.foxy.splash.ISplashPresenter
import com.app.foxy.splash.SplashPresenter
import dagger.Module
import dagger.Provides

@Module
class SplashModule {

    @Provides
    @SplashScope
    fun provideSplashPresenter(userService: IUserService, globalService: IGlobalService,
                               context: Context): ISplashPresenter =
            SplashPresenter(userService, globalService, context)
}
