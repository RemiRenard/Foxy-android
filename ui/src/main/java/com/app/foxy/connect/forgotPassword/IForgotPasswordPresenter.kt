package com.app.foxy.connect.forgotPassword

import com.app.foxy.IPresenter

/**
 * Interface of the forgot password presenter.
 */
interface IForgotPasswordPresenter : IPresenter<IForgotPasswordView> {

    fun forgotPassword(email: String)
}