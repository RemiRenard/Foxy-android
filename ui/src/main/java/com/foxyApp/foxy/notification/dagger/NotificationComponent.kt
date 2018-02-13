package com.foxyApp.foxy.notification.dagger

import dagger.Subcomponent
import com.foxyApp.foxy.notification.NotificationFragment
import com.foxyApp.foxy.notification.add.AddNotificationActivity
import com.foxyApp.foxy.notification.select_friends.SelectFriendsActivity

/**
 * Notification sub component.
 */
@NotificationScope
@Subcomponent(modules = arrayOf(NotificationModule::class))
interface NotificationComponent {

    // inject target here
    fun inject(target: NotificationFragment)

    fun inject(target: AddNotificationActivity)

    fun inject(target: SelectFriendsActivity)
}
