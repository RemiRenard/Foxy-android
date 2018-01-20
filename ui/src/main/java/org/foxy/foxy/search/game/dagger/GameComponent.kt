package org.foxy.foxy.search.game.dagger

import dagger.Subcomponent
import org.foxy.foxy.search.game.GameActivity

/**
 * Game sub component.
 */
@GameScope
@Subcomponent(modules = arrayOf(GameModule::class))
interface GameComponent {

    // inject target here
    fun inject(target: GameActivity)
}