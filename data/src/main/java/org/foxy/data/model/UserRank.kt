package org.foxy.data.model

/**
 * UserRank entity.
 */
class UserRank {

    var username: String? = null
    var avatar: String? = null
    var score: Int? = null
    var rank: Int? = null
    var type: String? = null

    constructor()

    constructor(username: String, score: Int, avatar: String) {
        this.username = username
        this.score = score
        this.avatar = avatar
    }

    constructor(username: String, score: Int, avatar: String, rank: Int) {
        this.username = username
        this.score = score
        this.avatar = avatar
        this.rank = rank
    }
}
