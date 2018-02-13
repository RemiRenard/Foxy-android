package com.foxyApp.data.cache

import com.foxyApp.data.model.Achievement
import com.foxyApp.data.model.Notification
import com.foxyApp.data.model.User
import com.foxyApp.data.model.UserRank
import com.foxyApp.data.network.api_response.RankingResponse
import java.io.File

/**
 * In-memory model.
 */
object Cache {

    var notifications: List<Notification> = ArrayList()
    var token: String? = null
    var tmpNotification: Notification? = null
    var audioFile: File? = null
    var friends: List<User> = ArrayList()
    var achievements: List<Achievement> = ArrayList()
    var currentUser: User? = null
    var rankings: RankingResponse? = RankingResponse()
}