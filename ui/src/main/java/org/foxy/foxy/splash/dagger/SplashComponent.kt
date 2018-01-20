package org.foxy.foxy.splash.dagger

import dagger.Subcomponent
import org.foxy.foxy.splash.SplashActivity

/**
 * Splash sub component.
 */
@SplashScope
@Subcomponent(modules = arrayOf(SplashModule::class))
interface SplashComponent {

    // inject target here
    fun inject(target: SplashActivity)
}
