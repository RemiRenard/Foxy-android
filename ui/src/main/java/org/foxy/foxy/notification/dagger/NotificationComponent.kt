package org.foxy.foxy.notification.dagger

import dagger.Subcomponent
import org.foxy.foxy.notification.NotificationFragment
import org.foxy.foxy.notification.add.AddNotificationActivity
import org.foxy.foxy.notification.select_friends.SelectFriendsActivity

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
