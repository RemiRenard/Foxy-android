package org.foxy.foxy.profile.friends.add

import org.foxy.data.model.User
import org.foxy.foxy.IPresenter

/**
 * Interface of the AddFriends presenter.
 */
interface IAddFriendsPresenter : IPresenter<IAddFriendsView> {

    fun findUsers(username : String = "")

    fun addFriends(user: User)
}