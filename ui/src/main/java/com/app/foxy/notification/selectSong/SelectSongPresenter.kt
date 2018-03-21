package com.app.foxy.notification.selectSong

import android.content.Context
import android.widget.Toast
import com.app.data.model.Notification
import com.app.data.model.Song
import com.app.data.network.ExceptionHandler
import com.app.domain.services.global.IGlobalService
import com.app.domain.services.notification.INotificationService
import com.app.foxy.notification.dagger.NotificationScope
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.io.File

/**
 * Select song presenter
 */
@NotificationScope
class SelectSongPresenter(private val mNotificationService: INotificationService,
                          private val mContext: Context,
                          private val mGlobalService: IGlobalService) : ISelectSongPresenter {

    private var mView: ISelectSongView? = null
    private var mCompositeDisposable: CompositeDisposable? = null

    override fun attachView(view: ISelectSongView) {
        mCompositeDisposable = CompositeDisposable()
        mView = view
    }

    override fun detachView() {
        mCompositeDisposable?.clear()
        mView = null
    }

    override fun manageTutorial() {
        if (!mGlobalService.isSelectSongTutorialShowed(mContext)) {
            mView?.showTutorial()
            mGlobalService.selectSongTutorialShowed(mContext)
        }
    }

    override fun saveTmpNotification(message: String, songId: String, audioFile: File?) {
        mView?.showProgressBar()
        mNotificationService.saveTmpNotification(Notification(message, songId, "message"), audioFile).doOnComplete {
            mView?.hideProgressBar()
            mView?.openFriendsActivity()
        }.subscribe()
    }

    override fun saveTmpNotification(songId: String) {
        mView?.showProgressBar()
        mNotificationService.saveTmpNotification(Notification(String(), songId, "message"), null).doOnComplete {
            mView?.hideProgressBar()
            mView?.openFriendsActivity()
        }.subscribe()
    }

    override fun getSongs(forceNetworkRefresh: Boolean) {
        mNotificationService.getSongs(forceNetworkRefresh).subscribe(object : Observer<List<Song>> {
            override fun onComplete() {
                // Do nothing
            }

            override fun onSubscribe(d: Disposable) {
                mCompositeDisposable?.add(d)
            }

            override fun onNext(songs: List<Song>) {
                mView?.displaySongs(songs)
            }

            override fun onError(e: Throwable) {
                Toast.makeText(mContext, ExceptionHandler.getMessage(e, mContext), Toast.LENGTH_LONG).show()
            }

        })
    }

}
