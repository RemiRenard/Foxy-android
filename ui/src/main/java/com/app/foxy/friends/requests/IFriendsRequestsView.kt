package com.app.foxy.friends.requests

import com.app.data.network.apiResponse.FriendsRequestsResponse
import com.app.foxy.IView

/**
 * Interface of the friends requests view.
 */
interface IFriendsRequestsView : IView {

    fun displayFriendsRequests(friendsRequests: List<FriendsRequestsResponse>)

    fun friendRequestCompleted(request: FriendsRequestsResponse)
}