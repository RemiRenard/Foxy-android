package org.foxy.foxy.connect.dagger

import android.content.Context
import dagger.Module
import dagger.Provides
import org.foxy.domain.services.notification.INotificationService
import org.foxy.domain.services.user.IUserService
import org.foxy.foxy.connect.ConnectPresenter
import org.foxy.foxy.connect.IConnectPresenter
import org.foxy.foxy.connect.forgot_password.ForgotPasswordPresenter
import org.foxy.foxy.connect.forgot_password.IForgotPasswordPresenter

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
