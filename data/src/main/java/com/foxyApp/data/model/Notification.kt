package com.foxyApp.data.model

import java.io.Serializable
import java.util.*

/**
 * Notification entity.
 */
class Notification : Serializable {

    var id: String? = null
    var message: String? = null
    var keyword: String? = null
    var userSource: User? = null
    var createdAt: Date? = null
    var type: String? = null
    var song: String? = null
    var isRead: Boolean = false

    constructor()

    constructor(message: String, keyword: String) {
        this.message = message
        this.keyword = keyword
    }

    constructor(message: String, type: String, song: String) {
        this.message = message
        this.type = type
        this.song = song
    }

    constructor(message: String, userSource: User, song: String) {
        this.message = message
        this.userSource = userSource
        this.song = song
    }

    constructor(message: String, keyword: String, type: String, song: String) {
        this.message = message
        this.keyword = keyword
        this.type = type
        this.song = song
    }
}
