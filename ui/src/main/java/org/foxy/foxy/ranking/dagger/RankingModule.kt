package org.foxy.foxy.ranking.dagger

import android.content.Context
import dagger.Module
import dagger.Provides
import org.foxy.domain.services.ranking.IRankingService
import org.foxy.foxy.adapter.RankingAdapter
import org.foxy.foxy.ranking.IRankingPresenter
import org.foxy.foxy.ranking.RankingPresenter

@Module
class RankingModule {

    @Provides
    @RankingScope
    fun provideRankingPresenter(rankingService: IRankingService, context: Context): IRankingPresenter =
            RankingPresenter(rankingService, context)

    // Adapters
    @Provides
    @RankingScope
    fun provideRankingAdapter(context: Context): RankingAdapter = RankingAdapter(context)
}
