package org.foxy.foxy.notification.select_friends

import org.foxy.data.model.User
import org.foxy.foxy.IView

/**
 * Interface of the friends view.
 */
interface ISelectFriendsView : IView {

    fun notificationSent()

    fun displayFriends(friends: List<User>)

    fun enableButton(isEnable: Boolean)
}
