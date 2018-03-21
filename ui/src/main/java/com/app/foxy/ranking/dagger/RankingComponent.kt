package com.app.foxy.ranking.dagger

import com.app.foxy.ranking.RankingFragment
import com.app.foxy.ranking.subFragment.RankingDailyFragment
import com.app.foxy.ranking.subFragment.RankingGlobalFragment
import com.app.foxy.ranking.subFragment.RankingWeeklyFragment
import dagger.Subcomponent

/**
 * Ranking sub component.
 */
@RankingScope
@Subcomponent(modules = [(RankingModule::class)])
interface RankingComponent {

    // inject target here
    fun inject(target: RankingFragment)

    fun inject(target: RankingGlobalFragment)

    fun inject(rankingDailyFragment: RankingDailyFragment)

    fun inject(rankingWeeklyFragment: RankingWeeklyFragment)
}
