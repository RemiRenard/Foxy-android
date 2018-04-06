package com.app.foxy.notification.dagger

import android.content.Context
import com.app.domain.services.friend.IFriendService
import com.app.domain.services.global.IGlobalService
import com.app.domain.services.notification.INotificationService
import com.app.foxy.adapter.SelectFriendsAdapter
import com.app.foxy.notification.INotificationPresenter
import com.app.foxy.notification.NotificationPresenter
import com.app.foxy.notification.adapter.INotificationAdapterPresenter
import com.app.foxy.notification.adapter.NotificationAdapter
import com.app.foxy.notification.adapter.NotificationAdapterPresenter
import com.app.foxy.notification.recordVoice.IRecordVoicePresenter
import com.app.foxy.notification.recordVoice.RecordVoicePresenter
import com.app.foxy.notification.selectFriends.ISelectFriendsPresenter
import com.app.foxy.notification.selectFriends.SelectFriendsPresenter
import com.app.foxy.notification.selectSong.ISelectSongPresenter
import com.app.foxy.notification.selectSong.SelectSongPresenter
import com.app.foxy.notification.selectSong.adapter.ISongAdapterPresenter
import com.app.foxy.notification.selectSong.adapter.SongAdapter
import com.app.foxy.notification.selectSong.adapter.SongAdapterPresenter
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
    fun provideSelectSongPresenter(notificationService: INotificationService, context: Context,
                                   globalService: IGlobalService): ISelectSongPresenter =
            SelectSongPresenter(notificationService, context, globalService)

    @Provides
    @NotificationScope
    fun provideFriendsPresenter(context: Context, friendService: IFriendService,
                                notificationService: INotificationService): ISelectFriendsPresenter =
            SelectFriendsPresenter(context, friendService, notificationService)

    @Provides
    @NotificationScope
    fun provideSongAdapterPresenter(context: Context): ISongAdapterPresenter = SongAdapterPresenter(context)

    @Provides
    @NotificationScope
    fun provideNotificationAdapterPresenter(context: Context): INotificationAdapterPresenter =
            NotificationAdapterPresenter(context)

    @Provides
    @NotificationScope
    fun provideRecordVoicePresenter(notificationService: INotificationService): IRecordVoicePresenter =
            RecordVoicePresenter(notificationService)

    // Adapters
    @Provides
    @NotificationScope
    fun provideNotificationAdapter(presenter: INotificationAdapterPresenter): NotificationAdapter =
            NotificationAdapter(presenter)

    @Provides
    @NotificationScope
    fun provideSelectFriendsAdapter(): SelectFriendsAdapter = SelectFriendsAdapter()

    @Provides
    @NotificationScope
    fun provideSongAdapter(presenter: ISongAdapterPresenter): SongAdapter = SongAdapter(presenter)
}
