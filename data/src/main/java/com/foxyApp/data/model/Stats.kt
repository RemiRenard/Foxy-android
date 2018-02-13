package com.foxyApp.data.model

class Stats {

    var topSongs: List<Song> = ArrayList()
    var topFriends: List<Friend> = ArrayList()

    class Song {
        var uri: String? = null
        var nbUsed: Int? = null
        var name: String? = null
    }

    class Friend {
        var username: String? = null
        var nbNotifSent: Int? = null
    }

}
