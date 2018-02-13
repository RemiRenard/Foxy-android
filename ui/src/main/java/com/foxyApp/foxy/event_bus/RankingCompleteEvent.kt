package com.foxyApp.foxy.event_bus

import com.foxyApp.data.network.api_response.RankingResponse

/**
 * Triggered when the ranking is completed
 */
class RankingCompleteEvent(var rankingResponse: RankingResponse)