package com.foxyApp.foxy.splash.dagger

import android.content.Context
import com.foxyApp.domain.services.global.IGlobalService
import com.foxyApp.domain.services.user.IUserService
import com.foxyApp.foxy.splash.ISplashPresenter
import com.foxyApp.foxy.splash.SplashPresenter
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
