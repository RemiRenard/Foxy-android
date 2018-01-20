package org.foxy.data.model

import java.io.Serializable

/**
 * Notification entity.
 */
class Notification : Serializable {

    var id: String? = null
    var title: String? = null
    var content: String? = null
    var type: String? = null
    var song: String? = null
    var isRead: Boolean = false

    constructor()

    constructor(title: String, content: String) {
        this.title = title
        this.content = content
    }

    constructor(title: String, content: String, type: String, song: String) {
        this.title = title
        this.content = content
        this.type = type
        this.song = song
    }
}
