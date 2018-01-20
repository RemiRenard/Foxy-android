package org.foxy.foxy.splash.dagger

import android.content.Context
import dagger.Module
import dagger.Provides
import org.foxy.domain.services.user.IUserService
import org.foxy.foxy.splash.ISplashPresenter
import org.foxy.foxy.splash.SplashPresenter

@Module
class SplashModule {

    @Provides
    @SplashScope
    fun provideSplashPresenter(userService: IUserService, context: Context): ISplashPresenter =
            SplashPresenter(userService, context)
}
