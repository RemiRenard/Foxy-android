package com.app.foxy.notification

import com.app.foxy.IPresenter

/**
 * Interface of the notification presenter.
 */
interface INotificationPresenter : IPresenter<INotificationView> {

    fun getNotifications(forceNetworkRefresh: Boolean)

    fun markNotificationAsRead(notificationId: String)
}
