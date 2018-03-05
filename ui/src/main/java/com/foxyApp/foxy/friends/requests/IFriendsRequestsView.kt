package com.foxyApp.foxy.friends.requests

import com.foxyApp.data.network.apiResponse.FriendsRequestsResponse
import com.foxyApp.foxy.IView

/**
 * Interface of the friends requests view.
 */
interface IFriendsRequestsView : IView {

    fun displayFriendsRequests(friendsRequests: List<FriendsRequestsResponse>)

    fun friendRequestCompleted(request: FriendsRequestsResponse)
}