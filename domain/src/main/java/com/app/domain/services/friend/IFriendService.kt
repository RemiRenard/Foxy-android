package com.app.domain.services.friend

import com.app.data.model.User
import com.app.data.network.apiResponse.FriendsRequestsResponse
import com.app.data.network.apiResponse.SimpleSuccessResponse
import io.reactivex.Observable

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