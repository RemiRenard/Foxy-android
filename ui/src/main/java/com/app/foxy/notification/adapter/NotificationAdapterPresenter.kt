package com.app.foxy.notification.adapter

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import com.app.foxy.notification.dagger.NotificationScope
import java.io.IOException

/**
 * Notification adapter presenter
 */
@NotificationScope
class NotificationAdapterPresenter(val mContext: Context) : INotificationAdapterPresenter {

    private var mView: INotificationAdapterView? = null
    private var mPlayer: MediaPlayer? = null

    override fun attachView(view: INotificationAdapterView) {
        mView = view
    }

    override fun detachView() {
        mView = null
    }

    override fun playSong(songUrl: String) {
        mPlayer = MediaPlayer()
        try {
            mPlayer?.setDataSource(mContext, Uri.parse(songUrl))
            mPlayer?.prepareAsync()
            mPlayer?.setOnPreparedListener {
                mView?.itemLoadingCompleted()
                mPlayer?.start()
            }
            mPlayer?.setOnCompletionListener {
                mPlayer = null
                mView?.itemPlayingCompleted()
            }
        } catch (e: IOException) {
            Log.e(javaClass.simpleName, e.message)
        } catch (e: IllegalStateException) {
            Log.e(javaClass.simpleName, e.message)
        }
    }

    override fun stopSong() {
        mPlayer?.stop()
    }
}
