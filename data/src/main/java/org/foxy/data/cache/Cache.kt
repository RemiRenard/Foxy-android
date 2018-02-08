package org.foxy.data.cache

import org.foxy.data.model.Achievement
import org.foxy.data.model.Notification
import org.foxy.data.model.User
import org.foxy.data.model.UserRank
import org.foxy.data.network.api_response.RankingResponse
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