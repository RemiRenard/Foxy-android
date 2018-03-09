package com.foxyApp.foxy.notification.add

import android.content.Context
import android.widget.Toast
import com.foxyApp.data.model.Notification
import com.foxyApp.data.model.Song
import com.foxyApp.data.network.ExceptionHandler
import com.foxyApp.domain.services.notification.INotificationService
import com.foxyApp.foxy.notification.dagger.NotificationScope
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.io.File

/**
 * Add notification presenter
 */
@NotificationScope
class AddNotificationPresenter(private val mNotificationService: INotificationService,
                               private val mContext: Context) : IAddNotificationPresenter {

    private var mView: IAddNotificationView? = null
    private var mCompositeDisposable: CompositeDisposable? = null

    override fun attachView(view: IAddNotificationView) {
        mCompositeDisposable = CompositeDisposable()
        mView = view
    }

    override fun detachView() {
        mCompositeDisposable?.clear()
        mView = null
    }

    override fun saveTmpNotification(message: String, songId: String, type: String, audioFile: File?) {
        mView?.showProgressBar()
        mNotificationService.saveTmpNotification(Notification(message, songId, type), audioFile).doOnComplete {
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
