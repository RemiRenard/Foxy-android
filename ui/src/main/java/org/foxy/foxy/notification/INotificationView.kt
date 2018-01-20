package org.foxy.foxy.notification

import org.foxy.data.model.Notification
import org.foxy.foxy.IView

/**
 * Interface of the notification view.
 */
interface INotificationView : IView {

    fun displayNotifications(notifications: List<Notification>)
}
