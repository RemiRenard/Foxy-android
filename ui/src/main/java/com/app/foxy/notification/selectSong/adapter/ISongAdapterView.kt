package com.app.foxy.notification.selectSong.adapter

/**
 * Interface of the SongAdapterView.
 */
interface ISongAdapterView {

    fun updateItems()

    fun itemLoadingCompleted()

    fun itemPlayingCompleted()
}
