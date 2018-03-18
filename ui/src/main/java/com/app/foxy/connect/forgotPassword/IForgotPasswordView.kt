package com.app.foxy.connect.forgotPassword

import com.app.foxy.IView

/**
 * Interface of the forgot password view.
 */
interface IForgotPasswordView : IView {

    fun enableButton(isEnable: Boolean)

    fun resetPasswordComplete()
}