package com.app.foxy.ranking.dagger

import android.content.Context
import com.app.domain.services.global.IGlobalService
import com.app.domain.services.ranking.IRankingService
import com.app.foxy.adapter.RankingAdapter
import com.app.foxy.ranking.IRankingPresenter
import com.app.foxy.ranking.RankingPresenter
import dagger.Module
import dagger.Provides

@Module
class RankingModule {

    @Provides
    @RankingScope
    fun provideRankingPresenter(rankingService: IRankingService, context: Context, globalService: IGlobalService):
            IRankingPresenter = RankingPresenter(rankingService, context, globalService)

    // Adapters
    @Provides
    @RankingScope
    fun provideRankingAdapter(): RankingAdapter = RankingAdapter()
}
