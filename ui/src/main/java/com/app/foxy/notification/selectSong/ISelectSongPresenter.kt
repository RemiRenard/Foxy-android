package com.app.foxy.notification.selectSong

import com.app.foxy.IPresenter
import java.io.File

/**
 * Interface of the SelectSong presenter.
 */
interface ISelectSongPresenter : IPresenter<ISelectSongView> {

    fun saveTmpNotification(message: String, songId: String, audioFile: File?)

    fun saveTmpNotification(songId: String)

    fun getSongs(forceNetworkRefresh: Boolean)
}
