package com.foxyApp.foxy.connect.dagger

import android.content.Context
import dagger.Module
import dagger.Provides
import com.foxyApp.domain.services.notification.INotificationService
import com.foxyApp.domain.services.user.IUserService
import com.foxyApp.foxy.connect.ConnectPresenter
import com.foxyApp.foxy.connect.IConnectPresenter
import com.foxyApp.foxy.connect.forgot_password.ForgotPasswordPresenter
import com.foxyApp.foxy.connect.forgot_password.IForgotPasswordPresenter

@Module
class ConnectModule {

    @Provides
    @ConnectScope
    fun provideConnectPresenter(context: Context, userService: IUserService,
                                notificationService: INotificationService): IConnectPresenter =
            ConnectPresenter(context, userService, notificationService)

    @Provides
    @ConnectScope
    fun provideForgotPasswordPresenter(context: Context, userService: IUserService): IForgotPasswordPresenter =
            ForgotPasswordPresenter(context, userService)
}
