package com.foxyApp.foxy.notification

import com.foxyApp.foxy.IPresenter

/**
 * Interface of the notification presenter.
 */
interface INotificationPresenter : IPresenter<INotificationView> {

    fun getNotifications(forceNetworkRefresh: Boolean)

    fun markNotificationAsRead(notificationId: String)
}
