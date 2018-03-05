package com.foxyApp.foxy.notification.dagger

import com.foxyApp.foxy.notification.NotificationFragment
import com.foxyApp.foxy.notification.add.AddNotificationActivity
import com.foxyApp.foxy.notification.selectFriends.SelectFriendsActivity
import dagger.Subcomponent

/**
 * Notification sub component.
 */
@NotificationScope
@Subcomponent(modules = [(NotificationModule::class)])
interface NotificationComponent {

    // inject target here
    fun inject(target: NotificationFragment)

    fun inject(target: AddNotificationActivity)

    fun inject(target: SelectFriendsActivity)
}
