package com.app.domain.services.ranking

import com.app.data.network.apiResponse.RankingResponse
import io.reactivex.Observable


/**
 * Interface of the Ranking service which contains method called by the presenter.
 */
interface IRankingService {

    fun getRanking(): Observable<RankingResponse>
}
