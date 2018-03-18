package com.app.foxy.friends

import com.app.data.model.User
import com.app.data.network.apiResponse.FriendsRequestsResponse
import com.app.foxy.IView

/**
 * Interface of the friends view.
 */
interface IFriendsView : IView {

    fun displayFriends(friends: List<User>)

    fun displayFriendsRequests(friendsRequests: List<FriendsRequestsResponse>)
}