package org.foxy.foxy.ranking.global

import org.foxy.data.model.UserRank
import org.foxy.foxy.IView

/**
 * Interface of the profile view.
 */
interface IRankingGlobalView : IView {
    fun displayRankings(userRanks: List<UserRank>)
}
