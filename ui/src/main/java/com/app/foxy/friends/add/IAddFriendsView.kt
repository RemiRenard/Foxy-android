package com.app.foxy.friends.add

import com.app.data.model.User
import com.app.foxy.IView

/**
 * Interface of the AddFriends view.
 */
interface IAddFriendsView : IView {

    fun displayUsers(users: List<User>)

    fun addFriendsComplete(user: User)
}