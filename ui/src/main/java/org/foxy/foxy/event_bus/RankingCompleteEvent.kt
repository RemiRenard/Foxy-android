package org.foxy.foxy.event_bus

import org.foxy.data.network.api_response.RankingResponse

/**
 * Triggered when the ranking is completed
 */
class RankingCompleteEvent(var rankingResponse: RankingResponse)