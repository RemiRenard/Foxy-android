package com.app.domain.services.user

import android.content.Context
import android.util.Log
import com.app.data.Constants
import com.app.data.Data
import com.app.data.cache.Cache
import com.app.data.database.table.TableUser
import com.app.data.model.Stats
import com.app.data.model.User
import com.app.data.network.apiRequest.*
import com.app.data.network.apiResponse.ConnectResponse
import com.app.data.network.apiResponse.RankingResponse
import com.app.data.network.apiResponse.SimpleSuccessResponse
import com.app.domain.eventBus.NetworkErrorEvent
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody
import org.greenrobot.eventbus.EventBus
import java.io.File
import java.util.*

/**
 * Class UserService
 */
class UserService : IUserService {

    /**
     * Return an observable linked to an endpoint of the api to create an account
     */
    override fun createAccount(email: String, password: String, firstName: String, lastName: String,
                               username: String, birthday: Date): Observable<ConnectResponse> {
        return Observable.just(Constants.GCM)
                .flatMap {
                    Data.networkService!!
                            .createAccount(CreateAccountRequest(email, password, firstName, lastName, username,
                                    birthday, FirebaseInstanceId.getInstance().getToken(Constants.PROJECT_NUMBER, it)))
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * Return an observable linked to an endpoint of the api to login
     */
    override fun login(email: String, password: String): Observable<ConnectResponse> {
        return Observable.just(Constants.GCM)
                .flatMap {
                    Data.networkService!!
                            .login(ConnectRequest(email, password,
                                    FirebaseInstanceId.getInstance().getToken(Constants.PROJECT_NUMBER, it)))
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * Return an observable linked to an endpoint of the api to login with facebook
     */
    override fun loginFacebook(facebookId: String, facebookToken: String): Observable<ConnectResponse> {
        return Observable.just(Constants.GCM)
                .flatMap {
                    Data.networkService!!
                            .loginFacebook(ConnectWithFacebookRequest(facebookId, facebookToken,
                                    FirebaseInstanceId.getInstance().getToken(Constants.PROJECT_NUMBER, it)))
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * Clear token and current user
     */
    override fun logout(context: Context): Observable<SimpleSuccessResponse> {
        return Data.networkService!!
                .logout(Cache.token!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete {
                    clearCache()
                    setToken("", context)
                    Observable
                            .just(Data.database!!
                                    .delete(TableUser.DATABASE_TABLE_NAME, "isCurrent = 1")).subscribe()
                }
    }

    /**
     * Clear custom cache (object in memory)
     */
    private fun clearCache() {
        Cache.currentUser = null
        Cache.friends = ArrayList()
        Cache.rankings = RankingResponse()
        Cache.notifications = ArrayList()
    }

    /**
     * Return the token used to do api request
     */
    override fun getToken(): String = Cache.token.orEmpty()

    /**
     * Set the token used to do api request in cache and in shared preferences
     */
    override fun setToken(token: String?, context: Context) {
        Cache.token = token
        val editor = context.getSharedPreferences(Constants.APP_TOKEN_PREF, Context.MODE_PRIVATE)?.edit()
        editor?.putString(Constants.TOKEN, token)?.apply()
    }

    /**
     * Put the token which is in the shared preferences in the cache.
     */
    override fun refreshToken(context: Context) {
        Cache.token = context.getSharedPreferences(Constants.APP_TOKEN_PREF, Context.MODE_PRIVATE)
                .getString(Constants.TOKEN, "")
    }

    /**
     * Save the current user in the cache and in the database.
     */
    override fun saveCurrentUser(user: User?) {
        Cache.currentUser = user
        Observable.just(Data.database!!
                .insert(TableUser.DATABASE_TABLE_NAME, TableUser.insertUser(user, true)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    /**
     * Get the current user
     * @return an observable of a user.
     */
    override fun getCurrentUser(forceNetworkRefresh: Boolean): Observable<User> {
        return if (Cache.currentUser != null && !forceNetworkRefresh) {
            Observable.just(Cache.currentUser)
        } else {
            getCurrentUserFromNetwork(Cache.token!!)
                    .publish { network -> Observable.merge(network, getCurrentUserFromDb().takeUntil(network)) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }

    /**
     * Fetch the current user from the network.
     * @return an observable of a user.
     */
    private fun getCurrentUserFromNetwork(token: String): Observable<User> {
        return Data.networkService!!
                .getMyProfile(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext({ updateCurrentUserDb(it); Cache.currentUser = it })
                .onErrorReturn {
                    EventBus.getDefault().post(NetworkErrorEvent(it))
                    Cache.currentUser!!
                }
    }

    /**
     * Get current user from the database.
     * @return an observable of a user.
     */
    private fun getCurrentUserFromDb(): Observable<User> {
        return Data.database!!.createQuery(TableUser.DATABASE_TABLE_NAME, TableUser.getCurrentUser())
                .mapToOne { cursor ->
                    val user = User()
                    user.id = cursor.getString(cursor.getColumnIndexOrThrow(TableUser.TABLE_USER_ID))
                    user.email = cursor.getString(cursor.getColumnIndexOrThrow(TableUser.TABLE_USER_EMAIL))
                    user.firstName = cursor.getString(cursor.getColumnIndexOrThrow(TableUser.TABLE_USER_FIRST_NAME))
                    user.lastName = cursor.getString(cursor.getColumnIndexOrThrow(TableUser.TABLE_USER_LAST_NAME))
                    user.username = cursor.getString(cursor.getColumnIndexOrThrow(TableUser.TABLE_USER_USERNAME))
                    user.birthday = Date(cursor.getLong(cursor.getColumnIndexOrThrow(TableUser.TABLE_USER_BIRTHDAY)))
                    user.emailVerified = cursor.getInt(cursor.getColumnIndexOrThrow(TableUser.TABLE_USER_EMAIL_VERIFIED)) == 1
                    user.avatar = cursor.getString(cursor.getColumnIndexOrThrow(TableUser.TABLE_USER_AVATAR))
                    user.stats = Gson().fromJson(cursor.getString(cursor.getColumnIndexOrThrow(TableUser.TABLE_USER_STATS)), Stats::class.java)
                    Cache.currentUser = user
                    user
                }
                .flatMap {
                    Cache.currentUser = it
                    return@flatMap Observable.just(it)
                }
    }

    /**
     * Update the current user in the database.
     * @param user User.
     */
    private fun updateCurrentUserDb(user: User) {
        // Delete the current user from the database.
        Data.database?.delete(TableUser.DATABASE_TABLE_NAME, "isCurrent = 1")
        // Use transaction to dodge the spamming of subscribers.
        val transaction = Data.database?.newTransaction()
        try {
            // Insert the user in the database.
            Data.database?.insert(TableUser.DATABASE_TABLE_NAME, TableUser.insertUser(user, true))
            transaction?.markSuccessful()
        } catch (e: Exception) {
            Log.e(javaClass.simpleName, "updateCurrentUserDb: ", e)
        } finally {
            transaction?.end()
        }
    }

    /**
     * Get the users from the database
     * @return an observable of a list of users.
     */
    override fun findUsers(username: String?, limit: Int, skip: Int): Observable<List<User>> {
        return Data.networkService!!
                .findUsers(Cache.token!!, FindUsersRequest(username, limit, skip))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * Call the forgot password route
     * @param email String
     * @return an observable of a SimpleSuccessResponse object.
     */
    override fun forgotPassword(email: String): Observable<SimpleSuccessResponse> {
        return Data.networkService!!
                .forgotPassword(ForgotPasswordRequest(email))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * Update the profile picture of the current user.
     * @param picture File
     * @return an observable of a User object.
     */
    override fun updateProfilePicture(picture: File): Observable<User> {
        return Data.networkService!!
                .updateProfilePicture(Cache.token!!, RequestBody.create(MediaType.parse(Constants.MEDIA_TYPE_IMAGE), picture))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { saveCurrentUser(it) }
    }
}
