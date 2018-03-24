package com.app.foxy.friends.add

import com.app.data.model.User
import com.app.foxy.IPresenter

/**
 * Interface of the AddFriends presenter.
 */
interface IAddFriendsPresenter : IPresenter<IAddFriendsView> {

    fun findUsers(username : String?)

    fun addFriends(user: User)
}