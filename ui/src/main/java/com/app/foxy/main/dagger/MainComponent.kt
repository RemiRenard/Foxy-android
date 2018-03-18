package com.app.foxy.main.dagger

import com.app.foxy.main.MainActivity

import dagger.Subcomponent

/**
 * Main sub component.
 */
@MainScope
@Subcomponent(modules = [(MainModule::class)])
interface MainComponent {

    // inject target here
    fun inject(target: MainActivity)
}
