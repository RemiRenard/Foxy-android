package com.foxyApp.foxy.eventBus

import com.foxyApp.data.network.apiResponse.RankingResponse

/**
 * Triggered when the ranking is completed
 */
class RankingCompleteEvent(var rankingResponse: RankingResponse)