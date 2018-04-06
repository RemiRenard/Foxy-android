package com.app.foxy.notification.adapter

/**
 * Interface of the NotificationAdapterPresenter.
 */
interface INotificationAdapterPresenter {

    fun attachView(view: INotificationAdapterView)

    fun detachView()

    fun playSong(songUrl: String)

    fun stopSong()

    fun downloadSound(soundUri: String, soundId: String)
}
