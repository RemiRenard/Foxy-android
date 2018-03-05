package com.foxyApp.foxy.friends.requests

import com.foxyApp.data.network.apiResponse.FriendsRequestsResponse
import com.foxyApp.foxy.IPresenter

/**
 * Interface of the friends requests presenter.
 */
interface IFriendsRequestsPresenter : IPresenter<IFriendsRequestsView> {

    fun getFriendsRequests()

    fun acceptRequest(requestId: String, notificationId: String, request: FriendsRequestsResponse)

    fun declineRequest(requestId: String, notificationId: String, request: FriendsRequestsResponse)
}