package com.app.foxy.notification.selectFriends

import com.app.foxy.IPresenter

/**
 * Interface of the friends presenter.
 */
interface ISelectFriendsPresenter : IPresenter<ISelectFriendsView> {

    fun getFriends()

    fun sendNotification(userIds: List<String>)

    fun clearNotificationCache()
}
