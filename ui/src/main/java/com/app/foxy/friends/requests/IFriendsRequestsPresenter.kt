package com.app.foxy.friends.requests

import com.app.data.network.apiResponse.FriendsRequestsResponse
import com.app.foxy.IPresenter

/**
 * Interface of the friends requests presenter.
 */
interface IFriendsRequestsPresenter : IPresenter<IFriendsRequestsView> {

    fun getFriendsRequests()

    fun acceptRequest(requestId: String, notificationId: String, request: FriendsRequestsResponse)

    fun declineRequest(requestId: String, notificationId: String, request: FriendsRequestsResponse)
}