package org.foxy.foxy.connect.dagger

import dagger.Subcomponent
import org.foxy.foxy.connect.ConnectActivity
import org.foxy.foxy.connect.forgot_password.ForgotPasswordActivity
import org.foxy.foxy.connect.fragment.LoginFragment
import org.foxy.foxy.connect.fragment.SignupFragment

/**
 * Connect sub component.
 */
@ConnectScope
@Subcomponent(modules = arrayOf(ConnectModule::class))
interface ConnectComponent {

    // inject target here
    fun inject(target: ConnectActivity)

    fun inject(target: LoginFragment)

    fun inject(target: SignupFragment)

    fun inject(target: ForgotPasswordActivity)
}
