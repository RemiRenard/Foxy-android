package org.foxy.foxy.play.findit.lobby

import org.foxy.foxy.IPresenter

/**
 * Interface of the findit lobby presenter.
 */
interface IFinditLobbyPresenter : IPresenter<IFinditLobbyView> {

    fun disconnectWebSocket()
}
