package org.foxy.domain.services.user

import android.content.Context
import io.reactivex.Observable
import org.foxy.data.model.User
import org.foxy.data.network.api_response.ConnectResponse
import org.foxy.data.network.api_response.SimpleSuccessResponse
import java.io.File
import java.util.*

/**
 * Interface of the User service which contains method called by the presenter.
 */
interface IUserService {

    fun createAccount(email: String, password: String, firstName: String, lastName: String,
                      username: String, birthday: Date, deviceId: String): Observable<ConnectResponse>

    fun login(email: String, password: String, deviceId: String): Observable<ConnectResponse>

    fun loginFacebook(facebookId: String, facebookToken: String, deviceId: String): Observable<ConnectResponse>

    fun logout(context: Context): Observable<SimpleSuccessResponse>

    fun forgotPassword(email: String): Observable<SimpleSuccessResponse>

    fun getToken(): String

    fun setToken(token: String?, context: Context)

    fun refreshToken(context: Context)

    fun saveCurrentUser(user: User?)

    fun getCurrentUser(forceNetworkRefresh: Boolean): Observable<User>

    fun findUsers(username: String, limit: Int, skip: Int): Observable<List<User>>

    fun updateProfilePicture(picture: File): Observable<User>
}
