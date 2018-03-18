package com.app.foxy.splash.dagger

import dagger.Subcomponent
import com.app.foxy.splash.SplashActivity

/**
 * Splash sub component.
 */
@SplashScope
@Subcomponent(modules = [(SplashModule::class)])
interface SplashComponent {

    // inject target here
    fun inject(target: SplashActivity)
}
