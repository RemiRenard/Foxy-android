package org.foxy.foxy.notification.select_friends

import org.foxy.foxy.IPresenter

/**
 * Interface of the friends presenter.
 */
interface ISelectFriendsPresenter : IPresenter<ISelectFriendsView> {

    fun getFriends()

    fun sendNotification(userIds: List<String>)
}
