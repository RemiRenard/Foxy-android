package org.foxy.foxy.notification

import org.foxy.foxy.IPresenter

/**
 * Interface of the notification presenter.
 */
interface INotificationPresenter : IPresenter<INotificationView> {

    fun getNotifications(forceNetworkRefresh: Boolean)

    fun markNotificationAsRead(notificationId: String)
}
