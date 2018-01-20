package org.foxy.foxy.play.findit.dagger

import android.content.Context
import dagger.Module
import dagger.Provides
import org.foxy.domain.services.ws.IWsService
import org.foxy.foxy.play.findit.home.FinditHomePresenter
import org.foxy.foxy.play.findit.home.IFinditHomePresenter
import org.foxy.foxy.play.findit.lobby.FinditLobbyPresenter
import org.foxy.foxy.play.findit.lobby.IFinditLobbyPresenter

@Module
class FinditModule {

    // Presenters
    @Provides
    @FinditScope
    fun provideFinditHomePresenter(context: Context, wsService: IWsService):
            IFinditHomePresenter = FinditHomePresenter(context, wsService)

    @Provides
    @FinditScope
    fun provideFinditLobbyPresenter(context: Context, wsService: IWsService):
            IFinditLobbyPresenter = FinditLobbyPresenter(context, wsService)
}
