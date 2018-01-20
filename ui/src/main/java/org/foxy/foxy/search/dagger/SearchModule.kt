package org.foxy.foxy.search.dagger

import android.content.Context
import dagger.Module
import dagger.Provides
import org.foxy.domain.services.game.IGameService
import org.foxy.foxy.adapter.GameAdapter
import org.foxy.foxy.search.ISearchPresenter
import org.foxy.foxy.search.SearchPresenter

@Module
class SearchModule {

    // Presenters
    @Provides
    @SearchScope
    fun provideSearchPresenter(context: Context, gameService: IGameService):
            ISearchPresenter = SearchPresenter(context, gameService)

    // Adapters
    @Provides
    @SearchScope
    fun provideGameAdapter(context: Context): GameAdapter = GameAdapter(context)
}