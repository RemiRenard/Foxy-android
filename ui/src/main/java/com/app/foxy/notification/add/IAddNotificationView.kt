package com.app.foxy.notification.add

import com.app.data.model.Song
import com.app.foxy.IView

/**
 * Interface of the Add notification view.
 */
interface IAddNotificationView : IView {

    fun openFriendsActivity()

    fun displaySongs(songs: List<Song>)
}
