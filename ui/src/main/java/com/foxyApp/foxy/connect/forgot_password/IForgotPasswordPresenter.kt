package com.foxyApp.foxy.connect.forgot_password

import com.foxyApp.foxy.IPresenter

/**
 * Interface of the forgot password presenter.
 */
interface IForgotPasswordPresenter : IPresenter<IForgotPasswordView> {

    fun forgotPassword(email: String)
}