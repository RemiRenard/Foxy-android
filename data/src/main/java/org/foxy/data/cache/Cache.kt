package org.foxy.data.cache

import org.foxy.data.model.Game
import org.foxy.data.model.Notification
import org.foxy.data.model.User
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
    var currentUser: User? = null
    var games: List<Game> = ArrayList()
}