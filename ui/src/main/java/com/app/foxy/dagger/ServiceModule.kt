package com.app.foxy.dagger

import com.app.domain.services.friend.FriendService
import com.app.domain.services.friend.IFriendService
import com.app.domain.services.global.GlobalService
import com.app.domain.services.global.IGlobalService
import com.app.domain.services.notification.INotificationService
import com.app.domain.services.notification.NotificationService
import com.app.domain.services.ranking.IRankingService
import com.app.domain.services.ranking.RankingService
import com.app.domain.services.user.IUserService
import com.app.domain.services.user.UserService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * ServiceModule Class.
 */
@Module
class ServiceModule {

    @Singleton
    @Provides
    fun provideGlobalService(): IGlobalService = GlobalService()

    @Singleton
    @Provides
    fun provideUserService(): IUserService = UserService()

    @Singleton
    @Provides
    fun provideNotificationService(): INotificationService = NotificationService()

    @Singleton
    @Provides
    fun provideFriendService(): IFriendService = FriendService()

    @Singleton
    @Provides
    fun provideRankingService(): IRankingService = RankingService()
}
