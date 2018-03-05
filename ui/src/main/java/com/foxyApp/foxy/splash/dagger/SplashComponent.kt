package com.foxyApp.foxy.splash.dagger

import dagger.Subcomponent
import com.foxyApp.foxy.splash.SplashActivity

/**
 * Splash sub component.
 */
@SplashScope
@Subcomponent(modules = [(SplashModule::class)])
interface SplashComponent {

    // inject target here
    fun inject(target: SplashActivity)
}
