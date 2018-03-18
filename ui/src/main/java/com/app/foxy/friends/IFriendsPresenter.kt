package com.app.foxy.friends

import com.app.foxy.IPresenter

/**
 * Interface of the friends presenter.
 */
interface IFriendsPresenter : IPresenter<IFriendsView> {

    fun getFriends()

    fun getFriendsRequests()
}