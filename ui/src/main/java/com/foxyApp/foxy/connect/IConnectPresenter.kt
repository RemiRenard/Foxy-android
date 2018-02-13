package com.foxyApp.foxy.connect

import com.facebook.login.LoginResult

import com.foxyApp.foxy.IPresenter
import java.util.*

/**
 * Interface of the connect presenter.
 */
interface IConnectPresenter : IPresenter<IConnectView> {

    fun loginFacebook(loginResult: LoginResult)

    fun login(email: String, password: String)

    fun createAccount(email: String, password: String, firstName: String, lastName: String,
                      username: String, birthday: Date)
}
