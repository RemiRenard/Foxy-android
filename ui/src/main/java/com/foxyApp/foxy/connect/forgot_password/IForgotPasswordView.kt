package com.foxyApp.foxy.connect.forgot_password

import com.foxyApp.foxy.IView

/**
 * Interface of the forgot password view.
 */
interface IForgotPasswordView : IView {

    fun enableButton(isEnable: Boolean)

    fun resetPasswordComplete()
}