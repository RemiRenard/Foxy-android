package com.app.foxy.main.dagger

import android.content.Context
import com.app.domain.services.global.IGlobalService
import com.app.domain.services.user.IUserService
import com.app.foxy.main.IMainPresenter
import com.app.foxy.main.MainPresenter
import dagger.Module
import dagger.Provides

@Module
class MainModule {

    @Provides
    @MainScope
    fun provideMainPresenter(context: Context, userService: IUserService, globalService: IGlobalService): IMainPresenter =
            MainPresenter(context, userService, globalService)
}
