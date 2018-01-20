package org.foxy.foxy.search

import org.foxy.data.model.Game
import org.foxy.foxy.IView

/**
 * Interface of the search view.
 */
interface ISearchView : IView {

    fun displayGames(games: List<Game>)
}