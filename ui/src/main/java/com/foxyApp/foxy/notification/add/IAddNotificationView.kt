package com.foxyApp.foxy.notification.add

import com.foxyApp.data.model.Song
import com.foxyApp.foxy.IView

/**
 * Interface of the Add notification view.
 */
interface IAddNotificationView : IView {

    fun openFriendsActivity()

    fun displaySongs(songs: List<Song>)
}
