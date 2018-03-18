package com.app.foxy.notification

import com.app.data.model.Notification
import com.app.foxy.IView

/**
 * Interface of the notification view.
 */
interface INotificationView : IView {

    fun displayNotifications(notifications: List<Notification>)
}
