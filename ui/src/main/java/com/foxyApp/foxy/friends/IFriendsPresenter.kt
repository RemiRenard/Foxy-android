package com.foxyApp.foxy.friends

import com.foxyApp.foxy.IPresenter

/**
 * Interface of the friends presenter.
 */
interface IFriendsPresenter : IPresenter<IFriendsView> {

    fun getFriends()

    fun getFriendsRequests()
}