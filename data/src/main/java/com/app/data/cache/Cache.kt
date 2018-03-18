package com.app.data.cache

import com.app.data.model.Achievement
import com.app.data.model.Notification
import com.app.data.model.Song
import com.app.data.model.User
import com.app.data.network.apiResponse.RankingResponse
import java.io.File

/**
 * In-memory model.
 */
object Cache {

    var notifications: List<Notification> = ArrayList()
    var songs: List<Song> = ArrayList()
    var token: String? = null
    var tmpNotification: Notification? = null
    var audioFile: File? = null
    var friends: List<User> = ArrayList()
    var achievements: List<Achievement> = ArrayList()
    var currentUser: User? = null
    var rankings: RankingResponse? = RankingResponse()
}