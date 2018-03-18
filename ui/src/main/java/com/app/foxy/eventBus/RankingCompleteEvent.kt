package com.app.foxy.eventBus

import com.app.data.network.apiResponse.RankingResponse

/**
 * Triggered when the ranking is completed
 */
class RankingCompleteEvent(var rankingResponse: RankingResponse)