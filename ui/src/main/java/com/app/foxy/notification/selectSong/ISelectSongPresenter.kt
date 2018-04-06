package com.app.foxy.notification.selectSong

import com.app.foxy.IPresenter
import java.io.File

/**
 * Interface of the SelectSong presenter.
 */
interface ISelectSongPresenter : IPresenter<ISelectSongView> {

    fun saveTmpNotification(songId: String)

    fun getSongs(forceNetworkRefresh: Boolean)

    fun manageTutorial()
}
