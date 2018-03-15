package com.foxyApp.foxy.connect.dagger

import android.content.Context
import com.foxyApp.domain.services.user.IUserService
import com.foxyApp.foxy.connect.ConnectPresenter
import com.foxyApp.foxy.connect.IConnectPresenter
import com.foxyApp.foxy.connect.forgotPassword.ForgotPasswordPresenter
import com.foxyApp.foxy.connect.forgotPassword.IForgotPasswordPresenter
import dagger.Module
import dagger.Provides

@Module
class ConnectModule {

    @Provides
    @ConnectScope
    fun provideConnectPresenter(context: Context, userService: IUserService): IConnectPresenter =
            ConnectPresenter(context, userService)

    @Provides
    @ConnectScope
    fun provideForgotPasswordPresenter(context: Context, userService: IUserService): IForgotPasswordPresenter =
            ForgotPasswordPresenter(context, userService)
}
