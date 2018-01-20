package org.foxy.foxy.play.findit.dagger

import dagger.Subcomponent
import org.foxy.foxy.play.findit.home.FinditHomeActivity
import org.foxy.foxy.play.findit.lobby.FinditLobbyActivity

/**
 * Findit sub component.
 */
@FinditScope
@Subcomponent(modules = arrayOf(FinditModule::class))
interface FinditComponent {

    // inject target here
    fun inject(target: FinditHomeActivity)

    fun inject(target: FinditLobbyActivity)
}
