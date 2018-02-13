package com.foxyApp.foxy.ranking.dagger

import android.content.Context
import dagger.Module
import dagger.Provides
import com.foxyApp.domain.services.ranking.IRankingService
import com.foxyApp.foxy.adapter.RankingAdapter
import com.foxyApp.foxy.ranking.IRankingPresenter
import com.foxyApp.foxy.ranking.RankingPresenter

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
