package com.app.domain.services.friend

import android.util.Log
import com.app.data.Data
import com.app.data.cache.Cache
import com.app.data.database.table.TableFriend
import com.app.data.model.User
import com.app.data.network.apiResponse.FriendsRequestsResponse
import com.app.data.network.apiResponse.SimpleSuccessResponse
import com.app.data.network.apiRequest.AddFriendRequest
import com.app.data.network.apiRequest.FriendRequestRequest
import com.app.domain.eventBus.NetworkErrorEvent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus

/**
 * Class FriendService
 */
class FriendService : IFriendService {

    private var mFriends = ArrayList<User>()

    /**
     * Get the list of users from the database first and when users from the network are fetched,
     * the list of users is up to date.
     * @return an observable of a list of users.
     */
    override fun getFriends(): Observable<List<User>> {
        mFriends.clear()
        return getFriendsFromNetwork()
                .publish { network -> Observable.merge(network, getFriendsFromDb().takeUntil(network)) }
                .doOnNext { Cache.friends = it }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * Fetch users from the network.
     * @return an observable of a list of users.
     */
    private fun getFriendsFromNetwork(): Observable<List<User>> {
        return Data.networkService!!
                .getFriends(Cache.token!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext({ updateFriendDb(it) })
                .onErrorReturn {
                    EventBus.getDefault().post(NetworkErrorEvent(it))
                    mFriends
                }
    }

    /**
     * Get users from the database.
     * @return an observable of a list of users.
     */
    private fun getFriendsFromDb(): Observable<List<User>> {
        return Data.database!!.createQuery(TableFriend.DATABASE_TABLE_NAME, TableFriend.getFriends())
                .mapToList { cursor ->
                    val user = User()
                    user.id = cursor.getString(cursor.getColumnIndexOrThrow(TableFriend.TABLE_USER_ID))
                    user.email = cursor.getString(cursor.getColumnIndexOrThrow(TableFriend.TABLE_USER_EMAIL))
                    user.firstName = cursor.getString(cursor.getColumnIndexOrThrow(TableFriend.TABLE_USER_FIRST_NAME))
                    user.lastName = cursor.getString(cursor.getColumnIndexOrThrow(TableFriend.TABLE_USER_LAST_NAME))
                    user.username = cursor.getString(cursor.getColumnIndexOrThrow(TableFriend.TABLE_USER_USERNAME))
                    user.avatar = cursor.getString(cursor.getColumnIndexOrThrow(TableFriend.TABLE_USER_AVATAR))
                    mFriends.add(user)
                    user
                }
    }

    /**
     * Update users in the database.
     * @param users a list of users.
     */
    private fun updateFriendDb(users: List<User>) {
        // Delete all users from the database.
        Data.database?.delete(TableFriend.DATABASE_TABLE_NAME, "")
        // Use transaction to dodge the spamming of subscribers.
        val transaction = Data.database?.newTransaction()
        try {
            // Insert a user in the database.
            users.map { Data.database?.insert(TableFriend.DATABASE_TABLE_NAME, TableFriend.createFriend(it)) }
            transaction?.markSuccessful()
        } catch (e: Exception) {
            Log.e(javaClass.simpleName, "updateFriendDb: ", e)
        } finally {
            transaction?.end()
        }
    }

    /**
     * Send a friend request
     * @param user User targeted
     * @return an observable of a SimpleSuccessResponse object.
     */
    override fun sendFriendRequest(user: User): Observable<SimpleSuccessResponse> {
        return Data.networkService!!
                .addFriend(Cache.token!!, AddFriendRequest(user.id!!))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * Get friend requests
     * @return an observable of a List of FriendsRequestsResponse object.
     */
    override fun getFriendsRequests(): Observable<List<FriendsRequestsResponse>> {
        return Data.networkService!!
                .getFriendsRequests(Cache.token!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * Accept a friend request
     * @return an observable of a SimpleSuccessResponse object.
     */
    override fun acceptFriendRequest(friendRequestId: String, notificationId: String): Observable<SimpleSuccessResponse> {
        return Data.networkService!!
                .acceptFriendRequest(Cache.token!!, FriendRequestRequest(friendRequestId, notificationId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { Cache.friends = ArrayList() }
    }

    /**
     * Decline a friend request
     * @return an observable of a SimpleSuccessResponse object.
     */
    override fun declineFriendRequest(friendRequestId: String, notificationId: String): Observable<SimpleSuccessResponse> {
        return Data.networkService!!
                .declineFriendRequest(Cache.token!!, FriendRequestRequest(friendRequestId, notificationId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { Cache.friends = ArrayList() }
    }
}