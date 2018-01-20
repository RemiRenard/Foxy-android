package org.foxy.data.model

import java.io.Serializable

/**
 * Game entity.
 */
class Game : Serializable {

    var id: String? = null
    var title: String? = null
    var description: String? = null
    var picture: String? = null
    var rules: String? = null
    var requirements: ArrayList<String>? = ArrayList()
    var nbMinPlayer: Int? = null
    var nbMaxPlayer: Int? = null
}