package com.app.foxy.notification.dagger

import com.app.foxy.notification.NotificationFragment
import com.app.foxy.notification.selectSong.SelectSongActivity
import com.app.foxy.notification.selectFriends.SelectFriendsActivity
import dagger.Subcomponent

/**
 * Notification sub component.
 */
@NotificationScope
@Subcomponent(modules = [(NotificationModule::class)])
interface NotificationComponent {

    // inject target here
    fun inject(target: NotificationFragment)

    fun inject(target: SelectSongActivity)

    fun inject(target: SelectFriendsActivity)
}
