package com.foxyApp.foxy.notification

import com.foxyApp.data.model.Notification
import com.foxyApp.foxy.IView

/**
 * Interface of the notification view.
 */
interface INotificationView : IView {

    fun displayNotifications(notifications: List<Notification>)
}
