package com.foxyApp.foxy.friends.add

import com.foxyApp.data.model.User
import com.foxyApp.foxy.IPresenter

/**
 * Interface of the AddFriends presenter.
 */
interface IAddFriendsPresenter : IPresenter<IAddFriendsView> {

    fun findUsers(username : String = "")

    fun addFriends(user: User)
}