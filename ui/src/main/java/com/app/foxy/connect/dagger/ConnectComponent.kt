package com.app.foxy.connect.dagger

import dagger.Subcomponent
import com.app.foxy.connect.ConnectActivity
import com.app.foxy.connect.forgotPassword.ForgotPasswordActivity
import com.app.foxy.connect.fragment.LoginFragment
import com.app.foxy.connect.fragment.SignupFragment

/**
 * Connect sub component.
 */
@ConnectScope
@Subcomponent(modules = [(ConnectModule::class)])
interface ConnectComponent {

    // inject target here
    fun inject(target: ConnectActivity)

    fun inject(target: LoginFragment)

    fun inject(target: SignupFragment)

    fun inject(target: ForgotPasswordActivity)
}
