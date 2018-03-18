package com.app.data.network.apiResponse

import com.app.data.model.UserRank

/**
 * Response from the API when the user wants to get his ranking response.
 */
class RankingResponse {

    var currentUserData: UserRank? = null
    var globalRanking = ArrayList<UserRank>()
    var weeklyRanking = ArrayList<UserRank>()
    var dailyRanking = ArrayList<UserRank>()
}