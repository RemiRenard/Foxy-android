package com.foxyApp.foxy.connect.dagger

import dagger.Subcomponent
import com.foxyApp.foxy.connect.ConnectActivity
import com.foxyApp.foxy.connect.forgotPassword.ForgotPasswordActivity
import com.foxyApp.foxy.connect.fragment.LoginFragment
import com.foxyApp.foxy.connect.fragment.SignupFragment

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
