package com.app.foxy.notification.adapter

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Environment
import android.provider.Settings
import android.support.design.widget.Snackbar
import android.util.Log
import com.app.foxy.BuildConfig
import com.app.foxy.R
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
                // TODO Mark notification as read.
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

    override fun downloadSound(soundUri: String, soundId: String) {
        val request = DownloadManager.Request(Uri.parse(soundUri))
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                "foxy_sound_$soundId.mp3")
        // to notify when download is complete
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.allowScanningByMediaScanner()// if you want to be available from media players
        val manager = mContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)
    }
}
