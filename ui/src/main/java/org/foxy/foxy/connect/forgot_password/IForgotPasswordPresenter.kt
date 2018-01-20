package org.foxy.foxy.connect.forgot_password

import org.foxy.foxy.IPresenter

/**
 * Interface of the forgot password presenter.
 */
interface IForgotPasswordPresenter : IPresenter<IForgotPasswordView> {

    fun forgotPassword(email: String)
}