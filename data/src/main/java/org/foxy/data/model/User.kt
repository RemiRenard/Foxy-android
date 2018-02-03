package org.foxy.data.model

import java.util.*

/**
 * User entity.
 */
class User {

    var id: String? = null
    var email: String? = null
    var firstName: String? = null
    var lastName: String? = null
    var username: String? = null
    var birthday: Date? = null
    var emailVerified: Boolean? = null
    var avatar: String? = null
    var stats: Stats? = null

    constructor()

    constructor(username: String) {
        this.username = username
    }
}
