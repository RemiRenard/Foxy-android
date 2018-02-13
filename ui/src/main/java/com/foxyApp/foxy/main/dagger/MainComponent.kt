package com.foxyApp.foxy.main.dagger

import com.foxyApp.foxy.main.MainActivity

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
