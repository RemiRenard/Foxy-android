package com.app.data.network

import com.app.data.Constants
import com.app.data.model.*
import com.app.data.network.apiRequest.*
import com.app.data.network.apiResponse.ConnectResponse
import com.app.data.network.apiResponse.FriendsRequestsResponse
import com.app.data.network.apiResponse.RankingResponse
import com.app.data.network.apiResponse.SimpleSuccessResponse
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * Retrofit network service. Contains all the endpoints.
 */
interface NetworkService {

    // GLOBAL SECTION
    @GET("config")
    fun getConfig(): Observable<Config>

    // USER SECTION
    @POST("user/create-account")
    fun createAccount(@Body params: CreateAccountRequest): Observable<ConnectResponse>

    @POST("user/login")
    fun login(@Body params: ConnectRequest): Observable<ConnectResponse>

    @POST("user/loginFacebook")
    fun loginFacebook(@Body params: ConnectWithFacebookRequest): Observable<ConnectResponse>

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
                         @Part("songId") songId: RequestBody,
                         @Part("type") type: RequestBody,
                         @Part("userIds") userIds: RequestBody,
                         @Part("song\"; filename=\"song.mp3\"") audio: RequestBody?): Observable<SimpleSuccessResponse>

    @POST("notification/mark-as-read")
    fun markNotificationAsRead(@Header(Constants.AUTHORIZATION) token: String,
                               @Body params: NotificationIdRequest): Observable<SimpleSuccessResponse>

    //RANKING SECTION
    @GET("ranking")
    fun getRanking(@Header(Constants.AUTHORIZATION) token: String): Observable<RankingResponse>

    //ACHIEVEMENT SECTION
    @GET("achievements")
    fun getAchievements(@Header(Constants.AUTHORIZATION) token: String): Observable<List<Achievement>>

    //SONG SECTION
    @GET("songs")
    fun getSongs(@Header(Constants.AUTHORIZATION) token: String): Observable<List<Song>>
}
