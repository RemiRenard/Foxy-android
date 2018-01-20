package org.foxy.foxy.notification.dagger

import android.content.Context
import dagger.Module
import dagger.Provides
import org.foxy.domain.services.friend.IFriendService
import org.foxy.domain.services.notification.INotificationService
import org.foxy.foxy.adapter.NotificationAdapter
import org.foxy.foxy.adapter.SelectFriendsAdapter
import org.foxy.foxy.notification.INotificationPresenter
import org.foxy.foxy.notification.NotificationPresenter
import org.foxy.foxy.notification.add.AddNotificationPresenter
import org.foxy.foxy.notification.add.IAddNotificationPresenter
import org.foxy.foxy.notification.details.DetailsNotificationPresenter
import org.foxy.foxy.notification.details.IDetailsNotificationPresenter
import org.foxy.foxy.notification.select_friends.ISelectFriendsPresenter
import org.foxy.foxy.notification.select_friends.SelectFriendsPresenter

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

    @Provides
    @NotificationScope
    fun provideDetailsNotificationPresenter(context: Context): IDetailsNotificationPresenter =
            DetailsNotificationPresenter(context)

    // Adapters
    @Provides
    @NotificationScope
    fun provideNotificationAdapter(context: Context): NotificationAdapter = NotificationAdapter(context)

    @Provides
    @NotificationScope
    fun provideSelectFriendsAdapter(context: Context): SelectFriendsAdapter = SelectFriendsAdapter(context)
}
