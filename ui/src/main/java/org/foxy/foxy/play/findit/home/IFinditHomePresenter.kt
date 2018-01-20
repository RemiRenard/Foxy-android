package org.foxy.foxy.play.findit.home

import org.foxy.foxy.IPresenter

/**
 * Interface of the findit home presenter.
 */
interface IFinditHomePresenter : IPresenter<IFinditHomeView> {

    fun connectWebSocket()
}
