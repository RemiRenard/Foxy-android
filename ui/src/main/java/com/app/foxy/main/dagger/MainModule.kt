package com.app.foxy.main.dagger

import android.content.Context
import dagger.Module
import dagger.Provides
import com.app.domain.services.user.IUserService
import com.app.foxy.main.IMainPresenter
import com.app.foxy.main.MainPresenter

@Module
class MainModule {

    @Provides
    @MainScope
    fun provideMainPresenter(context: Context, userService: IUserService): IMainPresenter =
            MainPresenter(context, userService)
}
