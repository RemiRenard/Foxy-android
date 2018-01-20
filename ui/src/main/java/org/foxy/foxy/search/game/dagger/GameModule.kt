package org.foxy.foxy.search.game.dagger

import android.content.Context
import dagger.Module
import dagger.Provides
import org.foxy.foxy.search.game.GamePresenter
import org.foxy.foxy.search.game.IGamePresenter

@Module
class GameModule {

    // Presenters
    @Provides
    @GameScope
    fun provideGamePresenter(context: Context): IGamePresenter = GamePresenter(context)
}