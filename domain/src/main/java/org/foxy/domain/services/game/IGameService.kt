package org.foxy.domain.services.game

import io.reactivex.Observable
import org.foxy.data.model.Game

/**
 * Interface of the Game service which contains method called by the presenter.
 */
interface IGameService {

    fun getGames(forceNetworkRefresh: Boolean): Observable<List<Game>>
}