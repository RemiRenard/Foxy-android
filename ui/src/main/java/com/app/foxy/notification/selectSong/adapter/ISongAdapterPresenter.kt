package com.app.foxy.notification.selectSong.adapter

import com.app.data.model.Song

/**
 * Interface of the SongAdapterPresenter.
 */
interface ISongAdapterPresenter {

    fun attachView(view: ISongAdapterView)

    fun detachView()

    fun playSong(song: Song)

    fun songSelected(song: Song)

    fun stopSong()
}
