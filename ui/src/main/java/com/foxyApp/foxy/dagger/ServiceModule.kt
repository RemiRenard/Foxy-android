package com.foxyApp.foxy.dagger

import com.foxyApp.domain.services.friend.FriendService
import com.foxyApp.domain.services.friend.IFriendService
import com.foxyApp.domain.services.global.GlobalService
import com.foxyApp.domain.services.global.IGlobalService
import com.foxyApp.domain.services.notification.INotificationService
import com.foxyApp.domain.services.notification.NotificationService
import com.foxyApp.domain.services.ranking.IRankingService
import com.foxyApp.domain.services.ranking.RankingService
import com.foxyApp.domain.services.user.IUserService
import com.foxyApp.domain.services.user.UserService
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
