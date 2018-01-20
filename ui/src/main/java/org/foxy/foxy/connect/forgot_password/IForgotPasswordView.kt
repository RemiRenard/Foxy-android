package org.foxy.foxy.connect.forgot_password

import org.foxy.foxy.IView

/**
 * Interface of the forgot password view.
 */
interface IForgotPasswordView : IView {

    fun enableButton(isEnable: Boolean)

    fun resetPasswordComplete()
}