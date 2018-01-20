package org.foxy.foxy.main.dagger

import android.content.Context
import dagger.Module
import dagger.Provides
import org.foxy.domain.services.user.IUserService
import org.foxy.foxy.main.IMainPresenter
import org.foxy.foxy.main.MainPresenter

@Module
class MainModule {

    @Provides
    @MainScope
    fun provideMainPresenter(context: Context, userService: IUserService): IMainPresenter =
            MainPresenter(context, userService)
}
