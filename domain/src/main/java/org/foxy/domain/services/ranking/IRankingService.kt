package org.foxy.domain.services.ranking

import io.reactivex.Observable
import org.foxy.data.network.api_response.RankingResponse


/**
 * Interface of the Ranking service which contains method called by the presenter.
 */
interface IRankingService {

    fun getRanking(): Observable<RankingResponse>
}
