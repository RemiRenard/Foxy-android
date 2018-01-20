package org.foxy.foxy.search

import org.foxy.foxy.IPresenter

/**
 * Interface of the game presenter.
 */
interface ISearchPresenter : IPresenter<ISearchView> {

    fun getGames(forceNetworkRefresh: Boolean)
}