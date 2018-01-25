package org.foxy.foxy.friends

import org.foxy.foxy.IPresenter

/**
 * Interface of the friends presenter.
 */
interface IFriendsPresenter : IPresenter<IFriendsView> {

    fun getFriends()

    fun getFriendsRequests()
}