package org.foxy.foxy.ranking.dagger

import android.content.Context
import dagger.Module
import dagger.Provides
import org.foxy.domain.services.ranking.IRankingService
import org.foxy.domain.services.ranking.RankingService
import org.foxy.foxy.adapter.RankingAdapter
import org.foxy.foxy.ranking.IRankingPresenter
import org.foxy.foxy.ranking.RankingPresenter
import org.foxy.foxy.ranking.global.IRankingGlobalPresenter
import org.foxy.foxy.ranking.global.RankingGlobalPresenter

@Module
class RankingModule {

    @Provides
    @RankingScope
    fun provideRankingPresenter(context: Context): IRankingPresenter =
            RankingPresenter(context)

    @Provides
    @RankingScope
    fun provideRankingGlobalPresenter(rankingService: IRankingService, context: Context): IRankingGlobalPresenter =
            RankingGlobalPresenter(context, rankingService)

    // Adapters
    @Provides
    @RankingScope
    fun provideRankingAdapter(context: Context): RankingAdapter = RankingAdapter(context)
}
