package com.foxyApp.foxy.notification.select_friends

import com.foxyApp.data.model.User
import com.foxyApp.foxy.IView

/**
 * Interface of the friends view.
 */
interface ISelectFriendsView : IView {

    fun notificationSent()

    fun displayFriends(friends: List<User>)

    fun enableButton(isEnable: Boolean)
}
