package org.foxy.foxy.dagger

import dagger.Module
import dagger.Provides
import org.foxy.domain.services.friend.FriendService
import org.foxy.domain.services.friend.IFriendService
import org.foxy.domain.services.notification.INotificationService
import org.foxy.domain.services.notification.NotificationService
import org.foxy.domain.services.user.IUserService
import org.foxy.domain.services.user.UserService
import javax.inject.Singleton

/**
 * ServiceModule Class.
 */
@Module
class ServiceModule {

    @Singleton
    @Provides
    fun provideUserService(): IUserService = UserService()

    @Singleton
    @Provides
    fun provideNotificationService(): INotificationService = NotificationService()

    @Singleton
    @Provides
    fun provideFriendService(): IFriendService = FriendService()
}
