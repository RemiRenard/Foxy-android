package org.foxy.foxy.main.dagger

import org.foxy.foxy.main.MainActivity

import dagger.Subcomponent

/**
 * Main sub component.
 */
@MainScope
@Subcomponent(modules = arrayOf(MainModule::class))
interface MainComponent {

    // inject target here
    fun inject(target: MainActivity)
}
