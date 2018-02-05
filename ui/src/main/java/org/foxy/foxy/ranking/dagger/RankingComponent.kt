package org.foxy.foxy.ranking.dagger

import dagger.Subcomponent
import org.foxy.foxy.ranking.RankingFragment
import org.foxy.foxy.ranking.global.RankingGlobalFragment

/**
 * Ranking sub component.
 */
@RankingScope
@Subcomponent(modules = arrayOf(RankingModule::class))
interface RankingComponent {

    // inject target here
    fun inject(target: RankingFragment)

    // inject target here
    fun inject(target: RankingGlobalFragment)

}
