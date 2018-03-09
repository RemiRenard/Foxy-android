package com.foxyApp.data.model

import java.io.Serializable
import java.util.*

/**
 * Notification entity.
 */
class Notification : Serializable {

    var id: String? = null
    var message: String? = null
    var userSource: User? = null
    var createdAt: Date? = null
    var type: String? = null
    var song: String? = null
    var isRead: Boolean = false
    // only used to save a tmp notif before sending
    var songId: String? = null

    constructor()

    constructor(message: String, userSource: User, song: String) {
        this.message = message
        this.userSource = userSource
        this.song = song
    }

    constructor(message: String, songId: String, type: String) {
        this.message = message
        this.songId = songId
        this.type = type
    }
}
