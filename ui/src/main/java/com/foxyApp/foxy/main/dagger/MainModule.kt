package com.foxyApp.foxy.main.dagger

import android.content.Context
import dagger.Module
import dagger.Provides
import com.foxyApp.domain.services.user.IUserService
import com.foxyApp.foxy.main.IMainPresenter
import com.foxyApp.foxy.main.MainPresenter

@Module
class MainModule {

    @Provides
    @MainScope
    fun provideMainPresenter(context: Context, userService: IUserService): IMainPresenter =
            MainPresenter(context, userService)
}
