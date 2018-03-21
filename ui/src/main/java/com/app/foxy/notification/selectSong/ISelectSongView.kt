package com.app.foxy.notification.selectSong

import com.app.data.model.Song
import com.app.foxy.IView

/**
 * Interface of the SelectSong view.
 */
interface ISelectSongView : IView {

    fun openFriendsActivity()

    fun displaySongs(songs: List<Song>)

    fun showTutorial()
}
