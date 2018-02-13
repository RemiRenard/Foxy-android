package com.foxyApp.foxy.notification.select_friends

import com.foxyApp.foxy.IPresenter

/**
 * Interface of the friends presenter.
 */
interface ISelectFriendsPresenter : IPresenter<ISelectFriendsView> {

    fun getFriends()

    fun sendNotification(userIds: List<String>)

    fun clearNotificationCache()
}
