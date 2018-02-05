package org.foxy.data.model

import java.util.*

/**
 * User entity.
 */
class UserRank {

    var username: String? = null
    var avatar: String? = null
    var score: String? = null
    var rank: String? = null
    var type: String? = null

    constructor()

    constructor(username: String, score: String, avatar:String )
    {
        this.username = username
        this.score = score
        this.avatar = avatar
    }

    constructor(username: String, score: String, avatar:String, rank: String)
    {
        this.username = username
        this.score = score
        this.avatar = avatar
        this.rank = rank
    }
}
