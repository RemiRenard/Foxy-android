package com.foxyApp.domain.services.ranking

import com.foxyApp.data.network.apiResponse.RankingResponse
import io.reactivex.Observable


/**
 * Interface of the Ranking service which contains method called by the presenter.
 */
interface IRankingService {

    fun getRanking(): Observable<RankingResponse>
}
