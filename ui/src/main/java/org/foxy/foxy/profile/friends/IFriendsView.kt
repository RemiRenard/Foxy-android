package org.foxy.foxy.profile.friends

import org.foxy.data.model.User
import org.foxy.data.network.api_response.FriendsRequestsResponse
import org.foxy.foxy.IView

/**
 * Interface of the friends view.
 */
interface IFriendsView : IView {

    fun displayFriends(friends: List<User>)

    fun displayFriendsRequests(friendsRequests: List<FriendsRequestsResponse>)
}