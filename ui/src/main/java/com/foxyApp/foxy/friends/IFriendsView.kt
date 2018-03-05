package com.foxyApp.foxy.friends

import com.foxyApp.data.model.User
import com.foxyApp.data.network.apiResponse.FriendsRequestsResponse
import com.foxyApp.foxy.IView

/**
 * Interface of the friends view.
 */
interface IFriendsView : IView {

    fun displayFriends(friends: List<User>)

    fun displayFriendsRequests(friendsRequests: List<FriendsRequestsResponse>)
}