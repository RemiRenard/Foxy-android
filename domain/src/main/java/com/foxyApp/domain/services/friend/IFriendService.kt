package com.foxyApp.domain.services.friend

import io.reactivex.Observable
import com.foxyApp.data.model.User
import com.foxyApp.data.network.api_response.FriendsRequestsResponse
import com.foxyApp.data.network.api_response.SimpleSuccessResponse

/**
 * Interface of the Friend service which contains method called by the presenter.
 */
interface IFriendService {

    fun getFriends(): Observable<List<User>>

    fun sendFriendRequest(user: User): Observable<SimpleSuccessResponse>

    fun getFriendsRequests(): Observable<List<FriendsRequestsResponse>>

    fun acceptFriendRequest(friendRequestId: String, notificationId: String): Observable<SimpleSuccessResponse>

    fun declineFriendRequest(friendRequestId: String, notificationId: String): Observable<SimpleSuccessResponse>
}