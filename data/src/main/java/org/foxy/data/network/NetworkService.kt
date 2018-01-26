package org.foxy.data.network

import io.reactivex.Observable
import okhttp3.RequestBody
import org.foxy.data.Constants
import org.foxy.data.model.Notification
import org.foxy.data.model.User
import org.foxy.data.network.api_response.ConnectResponse
import org.foxy.data.network.api_response.FriendsRequestsResponse
import org.foxy.data.network.api_response.SimpleSuccessResponse
import org.foxy.data.network.api_resquest.*
import retrofit2.http.*

/**
 * Retrofit network service. Contains all the endpoints.
 */
interface NetworkService {

    // USER SECTION
    @POST("user/create-account")
    fun createAccount(@Body params: CreateAccountRequest): Observable<ConnectResponse>

    @POST("user/login")
    fun login(@Body params: ConnectRequest): Observable<ConnectResponse>

    @POST("user/forgot-password")
    fun forgotPassword(@Body params: ForgotPasswordRequest): Observable<SimpleSuccessResponse>

    @GET("user/logout")
    fun logout(@Header(Constants.AUTHORIZATION) token: String): Observable<SimpleSuccessResponse>

    @GET("user/me")
    fun getMyProfile(@Header(Constants.AUTHORIZATION) token: String): Observable<User>

    @Multipart
    @POST("user/update-avatar")
    fun updateProfilePicture(@Header(Constants.AUTHORIZATION) token: String,
                             @Part("avatar\"; filename=\"avatar.jpg\" ") avatar: RequestBody): Observable<User>

    @POST("users")
    fun findUsers(@Header(Constants.AUTHORIZATION) token: String,
                  @Body params: FindUsersRequest): Observable<List<User>>

    // FRIENDS SECTION
    @GET("friends")
    fun getFriends(@Header(Constants.AUTHORIZATION) token: String): Observable<List<User>>

    @GET("friend/requests")
    fun getFriendsRequests(@Header(Constants.AUTHORIZATION) token: String): Observable<List<FriendsRequestsResponse>>

    @POST("friend/add")
    fun addFriend(@Header(Constants.AUTHORIZATION) token: String,
                  @Body params: AddFriendRequest): Observable<SimpleSuccessResponse>

    @POST("friend/accept")
    fun acceptFriendRequest(@Header(Constants.AUTHORIZATION) token: String,
                            @Body params: FriendRequestRequest): Observable<SimpleSuccessResponse>

    @POST("friend/decline")
    fun declineFriendRequest(@Header(Constants.AUTHORIZATION) token: String,
                             @Body params: FriendRequestRequest): Observable<SimpleSuccessResponse>

    // NOTIFICATION SECTION
    @GET("notifications")
    fun getNotifications(@Header(Constants.AUTHORIZATION) token: String): Observable<List<Notification>>

    @Multipart
    @POST("notification/send")
    fun sendNotification(@Header(Constants.AUTHORIZATION) token: String,
                         @Part("message") message: RequestBody,
                         @Part("keyword") keyword: RequestBody,
                         @Part("type") type: RequestBody,
                         @Part("userIds") userIds: RequestBody,
                         @Part("song\"; filename=\"song.mp3\"") audio: RequestBody?): Observable<SimpleSuccessResponse>

    @POST("notification/mark-as-read")
    fun markNotificationAsRead(@Header(Constants.AUTHORIZATION) token: String,
                               @Body params: NotificationIdRequest): Observable<SimpleSuccessResponse>

    //RANKING SECTION

}
