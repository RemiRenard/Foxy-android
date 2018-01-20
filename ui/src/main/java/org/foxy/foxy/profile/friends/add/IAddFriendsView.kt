package org.foxy.foxy.profile.friends.add

import org.foxy.data.model.User
import org.foxy.foxy.IView

/**
 * Interface of the AddFriends view.
 */
interface IAddFriendsView : IView {

    fun displayUsers(users: List<User>)

    fun addFriendsComplete(user: User)
}