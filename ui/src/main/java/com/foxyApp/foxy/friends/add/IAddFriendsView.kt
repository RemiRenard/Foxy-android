package com.foxyApp.foxy.friends.add

import com.foxyApp.data.model.User
import com.foxyApp.foxy.IView

/**
 * Interface of the AddFriends view.
 */
interface IAddFriendsView : IView {

    fun displayUsers(users: List<User>)

    fun addFriendsComplete(user: User)
}