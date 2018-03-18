package com.app.foxy.notification.dagger

import android.content.Context
import com.app.domain.services.friend.IFriendService
import com.app.domain.services.notification.INotificationService
import com.app.foxy.adapter.NotificationAdapter
import com.app.foxy.adapter.SelectFriendsAdapter
import com.app.foxy.adapter.SongAdapter
import com.app.foxy.notification.INotificationPresenter
import com.app.foxy.notification.NotificationPresenter
import com.app.foxy.notification.add.AddNotificationPresenter
import com.app.foxy.notification.add.IAddNotificationPresenter
import com.app.foxy.notification.selectFriends.ISelectFriendsPresenter
import com.app.foxy.notification.selectFriends.SelectFriendsPresenter
import dagger.Module
import dagger.Provides

@Module
class NotificationModule {

    // Presenters
    @Provides
    @NotificationScope
    fun provideNotificationPresenter(context: Context, notificationService: INotificationService):
            INotificationPresenter = NotificationPresenter(context, notificationService)

    @Provides
    @NotificationScope
    fun provideAddNotificationPresenter(notificationService: INotificationService, context: Context): IAddNotificationPresenter =
            AddNotificationPresenter(notificationService, context)

    @Provides
    @NotificationScope
    fun provideFriendsPresenter(context: Context, friendService: IFriendService,
                                notificationService: INotificationService): ISelectFriendsPresenter =
            SelectFriendsPresenter(context, friendService, notificationService)

    // Adapters
    @Provides
    @NotificationScope
    fun provideNotificationAdapter(context: Context): NotificationAdapter = NotificationAdapter(context)

    @Provides
    @NotificationScope
    fun provideSelectFriendsAdapter(context: Context): SelectFriendsAdapter = SelectFriendsAdapter(context)

    @Provides
    @NotificationScope
    fun provideSongAdapter(context: Context): SongAdapter = SongAdapter(context)
}
