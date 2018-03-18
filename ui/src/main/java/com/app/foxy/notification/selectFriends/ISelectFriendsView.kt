package com.app.foxy.notification.selectFriends

import com.app.data.model.User
import com.app.foxy.IView

/**
 * Interface of the friends view.
 */
interface ISelectFriendsView : IView {

    fun notificationSent()

    fun displayFriends(friends: List<User>)

    fun enableButton(isEnable: Boolean)
}
