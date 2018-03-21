package com.app.foxy.notification.selectSong.adapter

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import com.app.data.model.Song
import com.app.foxy.eventBus.SongSelectedNotifEvent
import com.app.foxy.notification.dagger.NotificationScope
import org.greenrobot.eventbus.EventBus
import java.io.IOException

/**
 * Song adapter presenter
 */
@NotificationScope
class SongAdapterPresenter(val mContext: Context) : ISongAdapterPresenter {

    private var mView: ISongAdapterView? = null
    private var mPlayer: MediaPlayer? = null

    override fun attachView(view: ISongAdapterView) {
        mView = view
    }

    override fun detachView() {
        mView = null
    }

    override fun playSong(song: Song) {
        mPlayer = MediaPlayer()
        try {
            mPlayer?.setDataSource(song.url)
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

    override fun songSelected(song: Song) {
        EventBus.getDefault().post(SongSelectedNotifEvent(song))
    }
}
