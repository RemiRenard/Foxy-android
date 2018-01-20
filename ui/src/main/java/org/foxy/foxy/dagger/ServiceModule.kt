package org.foxy.foxy.dagger

import dagger.Module
import dagger.Provides
import org.foxy.domain.services.friend.FriendService
import org.foxy.domain.services.friend.IFriendService
import org.foxy.domain.services.game.GameService
import org.foxy.domain.services.game.IGameService
import org.foxy.domain.services.notification.INotificationService
import org.foxy.domain.services.notification.NotificationService
import org.foxy.domain.services.user.IUserService
import org.foxy.domain.services.user.UserService
import org.foxy.domain.services.ws.IWsService
import org.foxy.domain.services.ws.WsService
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
    fun provideGameService(): IGameService = GameService()

    @Singleton
    @Provides
    fun provideWsService(): IWsService = WsService()

    @Singleton
    @Provides
    fun provideFriendService(): IFriendService = FriendService()
}
