package com.foxyApp.foxy.notification.dagger

import android.content.Context
import dagger.Module
import dagger.Provides
import com.foxyApp.domain.services.friend.IFriendService
import com.foxyApp.domain.services.notification.INotificationService
import com.foxyApp.foxy.adapter.NotificationAdapter
import com.foxyApp.foxy.adapter.SelectFriendsAdapter
import com.foxyApp.foxy.notification.INotificationPresenter
import com.foxyApp.foxy.notification.NotificationPresenter
import com.foxyApp.foxy.notification.add.AddNotificationPresenter
import com.foxyApp.foxy.notification.add.IAddNotificationPresenter
import com.foxyApp.foxy.notification.select_friends.ISelectFriendsPresenter
import com.foxyApp.foxy.notification.select_friends.SelectFriendsPresenter

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
}
