package com.foxyApp.foxy.ranking.dagger

import dagger.Subcomponent
import com.foxyApp.foxy.ranking.RankingFragment
import com.foxyApp.foxy.ranking.subFragment.RankingGlobalFragment

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
