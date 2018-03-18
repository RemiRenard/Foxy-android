package com.app.foxy.ranking.dagger

import dagger.Subcomponent
import com.app.foxy.ranking.RankingFragment
import com.app.foxy.ranking.subFragment.RankingGlobalFragment

/**
 * Ranking sub component.
 */
@RankingScope
@Subcomponent(modules = [(RankingModule::class)])
interface RankingComponent {

    // inject target here
    fun inject(target: RankingFragment)

    // inject target here
    fun inject(target: RankingGlobalFragment)

}
