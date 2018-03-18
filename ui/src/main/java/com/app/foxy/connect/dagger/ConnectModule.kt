package com.app.foxy.connect.dagger

import android.content.Context
import com.app.domain.services.user.IUserService
import com.app.foxy.connect.ConnectPresenter
import com.app.foxy.connect.IConnectPresenter
import com.app.foxy.connect.forgotPassword.ForgotPasswordPresenter
import com.app.foxy.connect.forgotPassword.IForgotPasswordPresenter
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
