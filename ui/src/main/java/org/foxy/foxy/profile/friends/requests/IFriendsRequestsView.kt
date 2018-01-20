package org.foxy.foxy.profile.friends.requests

import org.foxy.data.network.api_response.FriendsRequestsResponse
import org.foxy.foxy.IView

/**
 * Interface of the friends requests view.
 */
interface IFriendsRequestsView : IView {

    fun displayFriendsRequests(friendsRequests: List<FriendsRequestsResponse>)

    fun friendRequestCompleted(request: FriendsRequestsResponse)
}