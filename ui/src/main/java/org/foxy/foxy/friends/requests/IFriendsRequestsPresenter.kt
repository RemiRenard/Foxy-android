package org.foxy.foxy.friends.requests

import org.foxy.data.network.api_response.FriendsRequestsResponse
import org.foxy.foxy.IPresenter

/**
 * Interface of the friends requests presenter.
 */
interface IFriendsRequestsPresenter : IPresenter<IFriendsRequestsView> {

    fun getFriendsRequests()

    fun acceptRequest(requestId: String, notificationId: String, request: FriendsRequestsResponse)

    fun declineRequest(requestId: String, notificationId: String, request: FriendsRequestsResponse)
}