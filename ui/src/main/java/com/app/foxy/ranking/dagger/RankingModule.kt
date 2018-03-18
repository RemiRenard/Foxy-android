package com.app.foxy.ranking.dagger

import android.content.Context
import dagger.Module
import dagger.Provides
import com.app.domain.services.ranking.IRankingService
import com.app.foxy.adapter.RankingAdapter
import com.app.foxy.ranking.IRankingPresenter
import com.app.foxy.ranking.RankingPresenter

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
